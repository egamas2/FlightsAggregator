package com.company.api.controller;

import com.company.api.domain.ErrorMessage;
import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.service.FlightsAggregatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
public class FlightsAggregatorApiController {

    @Autowired
    private FlightsAggregatorService service;
//    http://localhost:8080/search?origin=any&%20&destination=any&departureDate=any&returnDate=any&numberOfPassengers=any
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAggregatedFlights(HttpServletRequest request,
                                                  @RequestParam("origin") String origin,
                                                  @RequestParam("destination") String destination,
                                                  @RequestParam("departureDate") String departureDate,
                                                  @RequestParam("returnDate") String returnDate,
                                                  @RequestParam("numberOfPassengers") String numberOfPassengers) throws ExecutionException, InterruptedException {
        if (isValidRequest(origin, destination, departureDate, returnDate, numberOfPassengers)) {
            List<FlightAggregatorResponse> list = service.aggregateResults(
                    FlightAggregatorRequest.builder().origin(origin).
                            destination(destination).
                            departureDate(departureDate).
                            returnDate(returnDate).
                            numberOfPassengers(Integer.parseInt(numberOfPassengers)).build());

            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Wrong formatted parameters", "numberOfPassengers must be numeric and all the parameters are mandatory. Dates must be formatted as ISO_LOCAL_DATE"));
        }
    }

    private boolean isValidRequest( String origin,  String destination,  String departureDate,  String returnDate,  String numberOfPassengers) {
        if (StringUtils.isNumeric(numberOfPassengers) &&
                StringUtils.isNotEmpty(origin) &&
                StringUtils.isNotEmpty(destination) &&
                StringUtils.isNotEmpty(departureDate) &&
                StringUtils.isNotEmpty(returnDate) &&
                StringUtils.isNotEmpty(numberOfPassengers)) {

            try {
                DateTimeFormatter.ISO_LOCAL_DATE.parse(departureDate);
                DateTimeFormatter.ISO_LOCAL_DATE.parse(returnDate);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
