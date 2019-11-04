package com.company.api.service.external;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.mongerair.MongerAirRequest;
import com.company.api.domain.mongerair.MongerAirResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Getter
@Setter
public class MongerAirServiceI implements IExternalFlightService<MongerAirRequest,MongerAirResponse> {

    private RestTemplate restTemplateExternal;

    private String address;

    private ZoneId zoneId;
    Logger log = LoggerFactory.getLogger(MongerAirServiceI.class);


    public MongerAirServiceI(RestTemplate restTemplateExternal, String mongerAirAddress, ZoneId zoneId) {
        this.restTemplateExternal = restTemplateExternal;
        this.address = mongerAirAddress;
        this.zoneId = zoneId;
    }

    @Override
    public CompletableFuture<ResponseEntity<List<MongerAirResponse>>> searchFlights(FlightAggregatorRequest request) {
        return CompletableFuture.supplyAsync(
                () -> getRestTemplateExternal().exchange(
                        adaptRequestToService(request).formatGetRequest(getAddress()),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<MongerAirResponse>>() {
                        }));
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
