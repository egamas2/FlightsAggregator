package clean.dataProviders.in.hardJet;

import clean.core.entities.IExternalQuoteResponse;
import clean.core.entities.FlightQuoteResponse;
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
@NoArgsConstructor
public class HardJetQuoteResponse implements IExternalQuoteResponse {
    private String carrier;
    private double basePrice;
    private double tax;
    private double discount;
    private String departureAirportName;
    private String arrivalAirportName;
    private String outboundDateTime; //ISO_INSTANT
    private String inboundDateTime; //ISO_INSTANT
    @Override
    public FlightQuoteResponse adaptResponse(ZoneId zoneId) {
        return  FlightQuoteResponse.builder()
                .airline(carrier)
                .arrivalDate(adaptDate(inboundDateTime, zoneId))
                .departureDate(adaptDate(outboundDateTime, zoneId))
                .departureAirportCode(departureAirportName)
                .destinationAirportCode(arrivalAirportName)
                .fare(calculateTotalPrice())
                .supplier("HardJet").build();
    }

    private String adaptDate(String datetime, ZoneId zoneId) {
        //Convert ISO_INSTANT to ISO_DATE_TIME
        return DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.of(LocalDateTime.from(DateTimeFormatter.ISO_INSTANT.withZone(zoneId).parse(datetime)), zoneId));
    }

    @Override
    public String calculateTotalPrice() {
        return BigDecimal.valueOf(basePrice+tax).multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(discount))).divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN).setScale(2,RoundingMode.HALF_EVEN).toString();
    }

    @Override
    public int compareTo(IExternalQuoteResponse o) {
        if (o == null) {
            return 1;
        }
        return calculateTotalPrice().compareTo(o.calculateTotalPrice());
    }
}
