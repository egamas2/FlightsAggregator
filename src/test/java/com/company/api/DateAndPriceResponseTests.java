package com.company.api;

import com.company.api.domain.aggregator.FlightAggregatorResponse;
import com.company.api.util.TestUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DateAndPriceResponseTests {

    @Test
    public void givenValidToughJetResponseWhenTranslatedToFlightAggregatorResponseDatesAndPricesAsExpected(){
        final FlightAggregatorResponse translated = TestUtilities.CUMBERSOME_JET_HARD_JET_RESPONSE.translate(ZoneId.of("Europe/London"));
        //Requirement Double rounded to 2 decimals with tax remve and discount applied.
        assertEquals("Price is formatted as double (2 decimal positions)","67.50",translated.getFare());
        //Date requirements
        assertEquals("Dates has ISO_INSTANT format and are translated to ISO_DATE_TIME","2019-09-22T17:57:45.132+01:00[Europe/London]",translated.getDepartureDate());
    }

    @Test
    public void givenValidMongerAirResponseWhenTranslatedToBusyFlightResponseDatesAsExpected(){
        final FlightAggregatorResponse translated = TestUtilities.CUMBERSOME_JET_MONGER_AIR_RESPONSE.translate(ZoneId.of("Europe/London"));
        //Date requirements
        assertEquals("Dates has ISO_LOCAL_DATE format and are translated to ISO_DATE_TIME","2019-09-22T17:57:45.132+01:00[Europe/London]",translated.getDepartureDate());
    }

}
