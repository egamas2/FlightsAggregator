package clean.core.useCases;

import clean.core.entities.*;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FlightsAggregator {

    IProviderFactory providerFactory;

    IProviderChannelFactory providerChannelFactory;

    public FlightsAggregator(IProviderFactory providerFactory, IProviderChannelFactory providerChannelFactory) {
        this.providerFactory = providerFactory;
        this.providerChannelFactory = providerChannelFactory;
    }

    public List<FlightQuoteResponse> aggregateFlights(IQuoteRequest request, ZoneId zoneId) {
        List<IProvider> providers = providerFactory.listAllProviders();
        List<IChannel<IExternalQuoteRequest, IExternalQuoteResponse>> channels = providers.stream()
                .map(p -> providerChannelFactory.getChannel(p))
                .collect(Collectors.toList());
        return aggregateQuotesFrom(channels, request, zoneId);
    }

    public List<FlightQuoteResponse> aggregateQuotesFrom(List<IChannel<IExternalQuoteRequest, IExternalQuoteResponse>> channels, IQuoteRequest request, ZoneId zoneId) {
        Stream<CompletableFuture<List<IExternalQuoteResponse>>> completableFutureStream =
                channels.parallelStream()
                        .map(c -> CompletableFuture.supplyAsync(() -> c.retrieveFlightQuotes(c.adaptRequestToExternal(request), c.retrieveReturnType()))
                        .exceptionally(t -> null));
        List<CompletableFuture<List<IExternalQuoteResponse>>> futures = completableFutureStream.collect(Collectors.toList());
        List<List<IExternalQuoteResponse>> allQuotes = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                ).join();
        return adaptQuotes(allQuotes, zoneId);
    }

    private List<FlightQuoteResponse> adaptQuotes(List<List<IExternalQuoteResponse>> allQuotes, ZoneId zoneId) {
        return allQuotes.stream().filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(r -> (FlightQuoteResponse) r.adaptResponse(zoneId))
                .collect(Collectors.toList());
    }
}
