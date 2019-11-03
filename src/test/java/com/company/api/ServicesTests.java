package com.company.api;

import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.domain.mongerair.MongerAirResponse;
import com.company.api.domain.hardjet.HardJetResponse;
import com.company.api.service.external.HardJetService;
import com.company.api.util.TestUtilities;
import com.company.api.service.external.MongerAirService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ServicesTests {

    @Mock
    ZoneId zoneId;

    @Mock
    RestTemplate restTemplateExternal;

    private static final String mongerAirAddress = "http://localhost:8080/find";

    private static final String hardJetAddress = "http://localhost:8080/lookfor";

    @InjectMocks
    private MongerAirService mongerAirService;

    @InjectMocks
    private HardJetService hardJetService;


    @Before
    public void init() {
        mongerAirService.setAddress(mongerAirAddress);
        hardJetService.setAddress(hardJetAddress);
    }

    @Test
    public void mongerAirServiceSearchFlightsTest() throws ExecutionException, InterruptedException {
        ResponseEntity<List<MongerAirResponse>> response = new ResponseEntity<List<MongerAirResponse>>(HttpStatus.OK);
        Mockito.when(restTemplateExternal.exchange(
                Matchers.anyObject(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity> any(),
                Matchers.<ParameterizedTypeReference<List<MongerAirResponse>>> any()))
                .thenReturn(response);
        final CompletableFuture<List<FlightAggregatorResponse>> future = mongerAirService.searchFlights(TestUtilities.initialRequest);
        assertNotNull("Rest template called",future.get());

    }

    @Test
    public void hardJetServiceSearchFlightsTest() throws ExecutionException, InterruptedException {
        ResponseEntity<List<HardJetResponse>> response = new ResponseEntity<List<HardJetResponse>>(HttpStatus.OK);
        Mockito.when(restTemplateExternal.exchange(
                Matchers.anyObject(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity> any(),
                Matchers.<ParameterizedTypeReference<List<HardJetResponse>>> any()))
                .thenReturn(response);
        final CompletableFuture<List<FlightAggregatorResponse>> future = hardJetService.searchFlights(TestUtilities.initialRequest);
        assertNotNull("Rest template called",future.get());

    }


}
