package com.company.api.service.external;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.hardjet.HardJetRequest;
import com.company.api.domain.hardjet.HardJetResponse;
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
public class HardJetServiceI implements IExternalFlightService<HardJetRequest,HardJetResponse> {

    private RestTemplate restTemplateExternal;

    private String address;

    private ZoneId zoneId;

    Logger log = LoggerFactory.getLogger(HardJetServiceI.class);

    public HardJetServiceI(RestTemplate restTemplateExternal, String hardJetAddress, ZoneId zoneId) {
        this.restTemplateExternal = restTemplateExternal;
        this.address = hardJetAddress;
        this.zoneId=zoneId;
    }

    @Override
    public CompletableFuture<ResponseEntity<List<HardJetResponse>>> searchFlights(FlightAggregatorRequest request) {
        return CompletableFuture.supplyAsync(
                () -> getRestTemplateExternal().exchange(
                        adaptRequestToService(request).formatGetRequest(getAddress()),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<HardJetResponse>>() {
                        }));
    }

    public HardJetRequest adaptRequestToService(FlightAggregatorRequest flightAggregatorRequest) {
        return HardJetRequest.builder()
                .from(flightAggregatorRequest.getOrigin())
                .to(flightAggregatorRequest.getDestination())
                .inboundDate(flightAggregatorRequest.getReturnDate())
                .outboundDate(flightAggregatorRequest.getDepartureDate())
                .numberOfAdults(flightAggregatorRequest.getNumberOfPassengers())
                .build();
    }
}
