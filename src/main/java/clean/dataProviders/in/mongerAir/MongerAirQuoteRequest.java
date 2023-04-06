package clean.dataProviders.in.mongerAir;

import clean.core.entities.IExternalQuoteRequest;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongerAirQuoteRequest implements IExternalQuoteRequest {
    private String origin;
    private String destination;
    private String departureDate; //ISO_LOCAL_DATE
    private String returnDate; //ISO_LOCAL_DATE
    private int passengerCount;

    @Override
    public String getFrom() {
        return origin;
    }

    @Override
    public String getTo() {
        return destination;
    }

    @Override
    public String getOutboundDate() {
        return returnDate;
    }

    @Override
    public String getInboundDate() {
        return departureDate;
    }

    @Override
    public int getNumberOfAdults() {
        return passengerCount;
    }
}
