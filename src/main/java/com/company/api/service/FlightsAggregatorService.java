package com.company.api.service;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.service.external.HardJetService;
import com.company.api.service.external.MongerAirService;
import com.company.api.service.external.ExternalFlightService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class FlightsAggregatorService {

    @Autowired
    private RestTemplate restTemplateExternal;
    @Autowired
    private String mongerAirAddress;
    @Autowired
    private String hardJetAddress;
    @Autowired
    private ZoneId zoneId;

    public List<FlightAggregatorResponse> aggregateResults(FlightAggregatorRequest request) throws InterruptedException, ExecutionException {
        //Services should be autowired, but java.util.concurrent.CompletionException: java.lang.IllegalStateException: Expectations already declared was thrown the second time a test tries
        ExternalFlightService[] services = {
                new MongerAirService(restTemplateExternal, mongerAirAddress, zoneId),
                new HardJetService(restTemplateExternal, hardJetAddress, zoneId)
        };

        CompletableFuture<List<FlightAggregatorResponse>>[] futures = new CompletableFuture[services.length];
        for (int i = 0; i < services.length; i++) {
            futures[i] = services[i].searchFlights(request);
        }

        final List<List<FlightAggregatorResponse>> finishedFutures = Arrays.stream(futures)
                .map(CompletableFuture::join).collect(Collectors.toList());

        return finishedFutures.stream().flatMap(Collection::stream).sorted().collect(Collectors.toList());
    }
}


