package com.company.api.service.external;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.domain.hardjet.HardJetRequest;
import com.company.api.domain.hardjet.HardJetResponse;
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
public class HardJetService implements ExternalFlightService {

    private RestTemplate restTemplateExternal;

    private String address;

    private ZoneId zoneId;

    Logger log = LoggerFactory.getLogger(HardJetService.class);

    public HardJetService(RestTemplate restTemplateExternal, String hardJetAddress, ZoneId zoneId) {
        this.restTemplateExternal = restTemplateExternal;
        this.address = hardJetAddress;
        this.zoneId=zoneId;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<FlightAggregatorResponse>> searchFlights(FlightAggregatorRequest request) {
        try {
            ResponseEntity<List<HardJetResponse>> response = restTemplateExternal.exchange(
                    adaptRequestToService(request).formatGetRequest(address),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<HardJetResponse>>() {
                    });
            if (response!=null && response.getBody() != null && !response.getBody().isEmpty()) {
                return CompletableFuture.completedFuture(response.getBody().stream().map(r->r.translate(this.zoneId)).collect(Collectors.toList()));
            }
        } catch (RestClientException e) {
            log.error("Hard Jet Service is unavailable:",e);
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
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
