package com.company.api.domain.aggregator;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightAggregatorResponse implements Comparable<FlightAggregatorResponse> {
    private String airline;
    private String supplier;
    private String fare;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String departureDate; //ISO_DATE_TIME
    private String arrivalDate; //ISO_DATE_TIME

    @Override
    public int compareTo(FlightAggregatorResponse o) {
        if (o==null ) {
            return 1;
        }
        return Double.compare(Double.valueOf(fare), Double.valueOf(o.fare));
    }
}
