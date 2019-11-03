package com.company.api.service.external;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.domain.mongerair.MongerAirRequest;
import com.company.api.domain.mongerair.MongerAirResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class MongerAirService implements ExternalFlightService {

    private RestTemplate restTemplateExternal;

    private String address;

    private ZoneId zoneId;

    Logger log = LoggerFactory.getLogger(MongerAirService.class);

    public MongerAirService(RestTemplate restTemplateExternal, String mongerAirAddress, ZoneId zoneId) {
        this.restTemplateExternal = restTemplateExternal;
        this.address = mongerAirAddress;
        this.zoneId=zoneId;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<FlightAggregatorResponse>> searchFlights(FlightAggregatorRequest request) {
        try {
            ResponseEntity<List<MongerAirResponse>> response = restTemplateExternal.exchange(
                    adaptRequestToService(request).formatGetRequest(address),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MongerAirResponse>>() {
                    });
            if (response!=null && response.getBody()!=null&& !response.getBody().isEmpty()) {
                return CompletableFuture.completedFuture(response.getBody().stream().map(r->r.translate(this.zoneId)).collect(Collectors.toList()));
            }
        } catch (RestClientException e) {
            log.error("Monger Air service is unavailable",e);
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public MongerAirRequest adaptRequestToService(FlightAggregatorRequest flightAggregatorRequest) {
        return MongerAirRequest.builder()
                .origin(flightAggregatorRequest.getOrigin())
                .destination(flightAggregatorRequest.getDestination())
                .departureDate(flightAggregatorRequest.getDepartureDate())
                .returnDate(flightAggregatorRequest.getReturnDate())
                .passengerCount(flightAggregatorRequest.getNumberOfPassengers()).build();
    }

}
