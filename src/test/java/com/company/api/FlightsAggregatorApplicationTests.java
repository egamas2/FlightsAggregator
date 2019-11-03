package com.company.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.api.controller.FlightsAggregatorApiController;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.domain.mongerair.MongerAirResponse;
import com.company.api.util.TestUtilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class FlightsAggregatorApplicationTests {

    @Autowired
    private RestTemplate restTemplateExternal;

    @Autowired
    private FlightsAggregatorApiController flightsAggregatorApiController;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${flightAggregator.http.address}")
    private String flightAggregatorAddress; //my address

    @Value("${mongerAir.http.address}")
    private String mongerAirAddress;

    @Value("${hardJet.http.address}")
    private String hardJetAddress;


    @Before
    public void init() throws URISyntaxException, JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplateExternal);
        mockExternalServerResponse(TestUtilities.MONGER_AIR_REQUEST.formatGetRequest(mongerAirAddress), TestUtilities.MONGER_AIR_RESPONS, HttpStatus.OK);
        mockExternalServerResponse(TestUtilities.HARD_JET_REQUEST.formatGetRequest(hardJetAddress), TestUtilities.HARD_JET_RESPONS, HttpStatus.OK);
    }

    @Test
    public void contextLoads() {
        testMockServer(TestUtilities.MONGER_AIR_REQUEST.formatGetRequest(mongerAirAddress));
        testMockServer(TestUtilities.HARD_JET_REQUEST.formatGetRequest(hardJetAddress));
    }

    //Acceptance - integration test
    @Test
    public void givenAllServersRunningWhenGetIsCalledThenReturnsAggregatedList() throws ExecutionException, InterruptedException {

        final ResponseEntity<?> aggregatedFlights = flightsAggregatorApiController.getAggregatedFlights(null, TestUtilities.initialRequest.getOrigin(), TestUtilities.initialRequest.getDestination(),
                TestUtilities.initialRequest.getDepartureDate(), TestUtilities.initialRequest.getReturnDate(), String.valueOf(TestUtilities.initialRequest.getNumberOfPassengers()));

        List<FlightAggregatorResponse> responses = (List<FlightAggregatorResponse>) aggregatedFlights.getBody();
        Assert.assertNotNull(responses);
        //Requirement: combines two services
        Assert.assertEquals(4, responses.size());

        //Requirement combines two services ordered by price
        Assert.assertArrayEquals(TestUtilities.getOrderedListAllFlightsCombined().toArray(), responses.toArray());
    }

    @Test
    public void givenOneOfTheServicesNotAvailableWhenGetIsCalledThenReturnsAggregatedListContainingOnlyAvailableServer() throws ExecutionException, InterruptedException, URISyntaxException, JsonProcessingException {

        mockServer = MockRestServiceServer.createServer(restTemplateExternal);
        mockExternalServerResponse(TestUtilities.MONGER_AIR_REQUEST.formatGetRequest(mongerAirAddress), TestUtilities.MONGER_AIR_RESPONS, HttpStatus.GATEWAY_TIMEOUT);
        mockExternalServerResponse(TestUtilities.HARD_JET_REQUEST.formatGetRequest(hardJetAddress), TestUtilities.HARD_JET_RESPONS, HttpStatus.OK);


        final ResponseEntity<?> aggregatedFlights = flightsAggregatorApiController.getAggregatedFlights(null, TestUtilities.initialRequest.getOrigin(), TestUtilities.initialRequest.getDestination(),
                TestUtilities.initialRequest.getDepartureDate(), TestUtilities.initialRequest.getReturnDate(), String.valueOf(TestUtilities.initialRequest.getNumberOfPassengers()));

        List<FlightAggregatorResponse> responses = (List<FlightAggregatorResponse>) aggregatedFlights.getBody();
        Assert.assertNotNull(responses);
        //Test for failsafe
        Assert.assertEquals(2, responses.size());

    }

    private void testMockServer(String urlParams) {
        ResponseEntity<List<MongerAirResponse>> response = restTemplateExternal.exchange(
                urlParams,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MongerAirResponse>>() {
                });
        List<MongerAirResponse> responses = response.getBody();
        Assert.assertNotNull(responses);
        Assert.assertEquals(2, responses.size());
    }

    private void mockExternalServerResponse(String uriAndParams, List responses, HttpStatus httpStatus) throws URISyntaxException, JsonProcessingException {
        if (HttpStatus.OK == httpStatus) {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(uriAndParams)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(httpStatus)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(responses))
                    );
        } else {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(uriAndParams)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(httpStatus)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(Collections.EMPTY_LIST))
                    );
        }
    }
}
