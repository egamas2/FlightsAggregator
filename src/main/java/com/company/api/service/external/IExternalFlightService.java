package com.company.api.service.external;

import com.company.api.domain.IFlightsRequest;
import com.company.api.domain.IFlightsResponse;
import com.company.api.domain.aggregator.FlightAggregatorRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IExternalFlightService<S extends IFlightsRequest, T extends IFlightsResponse> {

    CompletableFuture<ResponseEntity<List<T>>> searchFlights(FlightAggregatorRequest request);

    ZoneId getZoneId();

    String getAddress();

    S adaptRequestToService(FlightAggregatorRequest flightAggregatorRequest);

    RestTemplate getRestTemplateExternal();
}
