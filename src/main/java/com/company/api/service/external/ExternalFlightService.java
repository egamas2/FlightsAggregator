package com.company.api.service.external;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ExternalFlightService {

     CompletableFuture<List<FlightAggregatorResponse>> searchFlights(FlightAggregatorRequest request);

}
