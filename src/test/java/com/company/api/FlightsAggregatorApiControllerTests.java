package com.company.api;

import com.company.api.controller.FlightsAggregatorApiController;
import com.company.api.service.FlightsAggregatorService;
import com.company.api.util.TestUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FlightsAggregatorApiControllerTests {

    @Mock
    private FlightsAggregatorService service;

    @InjectMocks
    private FlightsAggregatorApiController controller;

    @Test
    public void whenGetIsCalledThenServiceIsCalled() throws ExecutionException, InterruptedException {
        controller.getAggregatedFlights(Mockito.mock(HttpServletRequest.class),
                TestUtilities.initialRequest.getOrigin(),
                TestUtilities.initialRequest.getDestination(),
                TestUtilities.initialRequest.getDepartureDate(),
                TestUtilities.initialRequest.getReturnDate(),
                String.valueOf(TestUtilities.initialRequest.getNumberOfPassengers()));
        Mockito.verify(service).aggregateResults(Mockito.any());
    }
}
