package com.company.api.domain.mongerair;

import com.company.api.domain.IFlightsResponse;
import com.company.api.domain.aggregator.FlightAggregatorResponse;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class MongerAirResponse implements IFlightsResponse<MongerAirResponse> {

    private String airline;
    private double price;
    private String cabinclass;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String departureDate; //ISO_LOCAL_DATE_TIME
    private String arrivalDate; //ISO_LOCAL_DATE_TIME

    @Override
    public FlightAggregatorResponse translate(ZoneId zoneId) {
        return FlightAggregatorResponse.builder()
                .airline(airline)
                .arrivalDate(adaptDate(arrivalDate,zoneId))
                .departureDate(adaptDate(departureDate,zoneId))
                .departureAirportCode(departureAirportCode)
                .destinationAirportCode(destinationAirportCode)
                .fare(calculateTotalPrice())
                .supplier("MongerAir").build();
    }

    private String adaptDate(String dateStr, ZoneId zoneId) {
        //Convert ISO_LOCAL_DATE_TIME to ISO_DATE_TIME
        return DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.of(LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(dateStr)), zoneId));
    }

    @Override
    public String calculateTotalPrice() {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    @Override
    public int compareTo(MongerAirResponse o) {
        if (o == null) {
            return 1;
        }
        return calculateTotalPrice().compareTo(o.calculateTotalPrice());
    }
}
