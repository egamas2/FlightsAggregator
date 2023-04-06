package clean.dataProviders.in.mongerAir;

import clean.core.entities.IExternalQuoteRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class Util {
    public static String createGetLink(String address, IExternalQuoteRequest request) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address);
        if (!StringUtils.isEmpty(request.getFrom())) {
            builder.queryParam("origin", request.getFrom());
        }
        if (!StringUtils.isEmpty(request.getTo())) {
            builder.queryParam("destination", request.getTo());
        }
        if (!StringUtils.isEmpty(request.getOutboundDate())) {
            builder.queryParam("departureDate", request.getOutboundDate());
        }
        if (!StringUtils.isEmpty(request.getInboundDate())) {
            builder.queryParam("returnDate", request.getInboundDate());
        }
        builder.queryParam("passengerCount", request.getNumberOfAdults());
        return builder.toUriString();
    }
}
