package clean.util;

import clean.core.entities.FlightQuoteRequest;
import clean.core.entities.FlightQuoteResponse;
import clean.dataProviders.in.hardJet.HardJetQuoteRequest;
import clean.dataProviders.in.hardJet.HardJetQuoteResponse;
import clean.dataProviders.in.mongerAir.MongerAirQuoteRequest;
import clean.dataProviders.in.mongerAir.MongerAirQuoteResponse;

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

    public static final FlightQuoteRequest initialRequest = new FlightQuoteRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);

    public static final MongerAirQuoteResponse CUMBERSOME_JET_MONGER_AIR_RESPONSE = new MongerAirQuoteResponse("CumbersomeJet", 95, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL);

    public static final List<MongerAirQuoteResponse> MONGER_AIR_RESPONS = Arrays.asList(
            new MongerAirQuoteResponse("CumbersomeJet", 95, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL),
            new MongerAirQuoteResponse("RyanSky", 50, "Tourist", "LGW", "MAD", ISO_LOCAL_DATE_TIME_DEPARTURE, ISO_LOCAL_DATE_TIME_ARRIVAL)
    );

    public static final HardJetQuoteResponse CUMBERSOME_JET_HARD_JET_RESPONSE = new HardJetQuoteResponse("CumbersomeJet", 70, 5, 10, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL);

    public static final List<HardJetQuoteResponse> HARD_JET_RESPONS = Arrays.asList(
            new HardJetQuoteResponse("CumbersomeJet", 70, 5, 0, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL),
            new HardJetQuoteResponse("British AirPaths", 80, 5, 2, "LGW", "MAD", ISO_INSTANT_DEPARTURE, ISO_INSTANT_ARRIVAL)
    );

        public static final MongerAirQuoteRequest MONGER_AIR_REQUEST = new MongerAirQuoteRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);

        public static final HardJetQuoteRequest HARD_JET_REQUEST = new HardJetQuoteRequest("LGW", "MAD", ISO_LOCAL_DATE_DEPARTURE, ISO_LOCAL_DATE_ARRIVAL, 1);


        public static List<FlightQuoteResponse> getOrderedListAllFlightsCombined() {
            ZoneId zoneId = ZoneId.of("Europe/London");
            return Stream.concat(
                    MONGER_AIR_RESPONS.stream().map(r -> r.adaptResponse(zoneId)),
                    HARD_JET_RESPONS.stream().map(r -> r.adaptResponse(zoneId))).sorted().collect(Collectors.toList());

        }
}
