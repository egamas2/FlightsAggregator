package clean.integration;

import clean.core.entities.IExternalQuoteResponse;
import clean.core.useCases.FlightsAggregator;
import clean.core.entities.FlightQuoteResponse;
import clean.dataProviders.in.hardJet.HardJetQuoteResponse;
import clean.dataProviders.in.hardJet.Util;
import clean.dataProviders.in.mongerAir.MongerAirQuoteResponse;
import clean.dataProviders.out.config.FlightAggregatorFactory;
import clean.dataProviders.out.config.ProviderBeanFactory;
import clean.util.TestUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProviderBeanFactory.class, FlightAggregatorFactory.class})
public class FlightsAggregatorCleanIntegrationTests {
    @Autowired
    private RestTemplate restTemplateExternal;
    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${flightAggregator.http.address}")
    private String flightAggregatorAddress; //my address

    @Value("${mongerAir.http.address}")
    private String mongerAirAddress;

    @Value("${hardJet.http.address}")
    private String hardJetAddress;
    private String hardJetTestLink;
    private String mongerAirTestLink;

    @Autowired
    private FlightsAggregator aggregator;

    @Autowired
    private ZoneId zoneId;

    @Before
    public void init() throws URISyntaxException, JsonProcessingException {
        mockServer = MockRestServiceServer.bindTo(restTemplateExternal).ignoreExpectOrder(true).build();
        hardJetTestLink = Util.createGetLink(hardJetAddress, TestUtilities.HARD_JET_REQUEST);
        mongerAirTestLink = clean.dataProviders.in.mongerAir.Util.createGetLink(mongerAirAddress, TestUtilities.MONGER_AIR_REQUEST);
        mockExternalServerResponse(mockServer, hardJetTestLink, TestUtilities.HARD_JET_RESPONS, HttpStatus.OK);
        mockExternalServerResponse(mockServer, mongerAirTestLink, TestUtilities.MONGER_AIR_RESPONS, HttpStatus.OK);
    }

    private <T extends IExternalQuoteResponse> void testMongerAirResponseMockServer(String urlParams, ParameterizedTypeReference<List<T>> returnClass) {
        ResponseEntity<List<T>> response = restTemplateExternal.exchange(
                urlParams,
                HttpMethod.GET,
                null,
                returnClass);
        List<T> responses = response.getBody();
        Assert.assertNotNull(responses);
        Assert.assertEquals(2, responses.size());
    }

    @Test
    public void contextLoads() {
        testMongerAirResponseMockServer(mongerAirTestLink, new ParameterizedTypeReference<List<MongerAirQuoteResponse>>() {
        });
        testMongerAirResponseMockServer(hardJetTestLink, new ParameterizedTypeReference<List<HardJetQuoteResponse>>() {
        });
    }

    private void mockExternalServerResponse(MockRestServiceServer mockServer, String uriAndParams, List responses, HttpStatus httpStatus) throws URISyntaxException, JsonProcessingException {
        if (HttpStatus.OK == httpStatus) {
            mockServer.expect(ExpectedCount.between(1, 10),
                            requestTo(new URI(uriAndParams)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(httpStatus)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(responses))
                    );
        } else {
            mockServer.expect(ExpectedCount.between(1, 10),
                            requestTo(new URI(uriAndParams)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(httpStatus)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(Collections.EMPTY_LIST))
                    );
        }
    }

    @Test
    public void givenAllServersRunningWhenGetIsCalledThenReturnsAggregatedList() throws ExecutionException, InterruptedException {
        //
        List<FlightQuoteResponse> aggregatedFlights = aggregator.aggregateFlights(TestUtilities.initialRequest, zoneId);
        Assert.assertNotNull(aggregatedFlights);
        //Requirement: combines two services
        Assert.assertEquals(4, aggregatedFlights.size());

        //Requirement combines two services ordered by price
        Assert.assertArrayEquals(TestUtilities.getOrderedListAllFlightsCombined().toArray(), aggregatedFlights.stream().sorted().toArray());
    }

    @Test
    public void givenOneOfTheServicesNotAvailableWhenGetIsCalledThenReturnsAggregatedListContainingOnlyAvailableServer() throws ExecutionException, InterruptedException, URISyntaxException, JsonProcessingException {

        mockServer.reset();
        mockExternalServerResponse(mockServer, mongerAirTestLink, TestUtilities.MONGER_AIR_RESPONS, HttpStatus.GATEWAY_TIMEOUT);
        mockExternalServerResponse(mockServer, hardJetTestLink, TestUtilities.HARD_JET_RESPONS, HttpStatus.OK);

        List<FlightQuoteResponse> aggregatedFlights = aggregator.aggregateFlights(TestUtilities.initialRequest, zoneId);
        Assert.assertNotNull(aggregatedFlights);
        //Test for failsafe
        Assert.assertEquals(2, aggregatedFlights.size());

    }
}
