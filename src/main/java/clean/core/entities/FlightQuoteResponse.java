package clean.core.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightQuoteResponse implements IQuoteResponse , Comparable<FlightQuoteResponse>{
    private String airline;
    private String supplier;
    private String fare;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String departureDate; //ISO_DATE_TIME
    private String arrivalDate; //ISO_DATE_TIME

    @Override
    public int compareTo(FlightQuoteResponse o) {
        if (o==null ) {
            return 1;
        }
        return Double.compare(Double.parseDouble(fare), Double.parseDouble(o.fare));
    }
}
