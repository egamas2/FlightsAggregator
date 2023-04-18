package clean.core.entities;

import clean.core.entities.IQuoteRequest;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class FlightQuoteRequest implements IQuoteRequest {
    private String origin;
    private String destination;
    private String departureDate; //ISO_LOCAL_DATE
    private String returnDate; //ISO_LOCAL_DATE
    private int numberOfPassengers;

    
}
