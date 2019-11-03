package com.company.api.util;

import com.company.api.domain.aggregator.FlightAggregatorRequest;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.domain.mongerair.MongerAirRequest;
import com.company.api.domain.mongerair.MongerAirResponse;
import com.company.api.domain.hardjet.HardJetRequest;
import com.company.api.domain.hardjet.HardJetResponse;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestUtilities {

    public static final String ISO_LOCAL_DATE_DEPARTURE = "2019-09-22";
    public static final String ISO_LOCAL_DATE_ARRIVAL = "2019-09-23";
    public static final String ISO_LOCAL_DATE_TIME_DEPARTURE = "2019-09-22T17:57:45.132";
    public static final String ISO_LOCAL_DATE_TIME_ARRIVAL = "2019-09-23T17:57:45.132";
    public static final String ISO_INSTANT_DEPARTURE = "2019-09-22T16:57:45.132Z";
    public static final String ISO_INSTANT_ARRIVAL = "2019-09-23T16:57:45.132Z";

    public static final FlightAggregatorRequest initialRequest = new FlightAggregatorRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);

    public static final MongerAirResponse CUMBERSOME_JET_MONGER_AIR_RESPONSE =  new MongerAirResponse("CumbersomeJet", 95, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL);

    public static final List<MongerAirResponse> MONGER_AIR_RESPONS = Arrays.asList(
            new MongerAirResponse("CumbersomeJet", 95, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL),
            new MongerAirResponse("RyanSky", 50, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL)
    );

    public static final HardJetResponse CUMBERSOME_JET_HARD_JET_RESPONSE = new HardJetResponse("CumbersomeJet", 70, 5, 10, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL);

    public static final List<HardJetResponse> HARD_JET_RESPONS = Arrays.asList(
            new HardJetResponse("CumbersomeJet", 70, 5, 0, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL),
            new HardJetResponse("British AirPaths", 80, 5, 2, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL)
    );

    public static final MongerAirRequest MONGER_AIR_REQUEST = new MongerAirRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);

    public static final HardJetRequest HARD_JET_REQUEST = new HardJetRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);


    public static List<FlightAggregatorResponse> getOrderedListAllFlightsCombined() {
        ZoneId zoneId = ZoneId.of("Europe/London");
        return Stream.concat(
                MONGER_AIR_RESPONS.stream().map(r -> r.translate(zoneId)),
                HARD_JET_RESPONS.stream().map(r -> r.translate(zoneId))).sorted().collect(Collectors.toList());

    }

}
