package com.company.api.controller;

import com.company.api.util.TestUtilities;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//For external integration test. Not really needed for Junit tests
@RestController
public class MockExternalControllers {
    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMongerAirFlights(HttpServletRequest request,
                                                @RequestParam("origin") String origin,
                                                @RequestParam("destination") String destination,
                                                @RequestParam("departureDate") String departureDate,
                                                @RequestParam("returnDate") String returnDate,
                                                @RequestParam("passengerCount") String numberOfPassengers)  {
        return ResponseEntity.ok(TestUtilities.MONGER_AIR_RESPONS);
    }

    @RequestMapping(value = "/lookfor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHardJetFlights(HttpServletRequest request,
                                                @RequestParam("from") String origin,
                                                @RequestParam("to") String destination,
                                                @RequestParam("outboundDate") String departureDate,
                                                @RequestParam("inboundDate") String returnDate,
                                                @RequestParam("numberOfAdults") String numberOfPassengers)  {
        return ResponseEntity.ok(TestUtilities.HARD_JET_RESPONS);
    }
}
