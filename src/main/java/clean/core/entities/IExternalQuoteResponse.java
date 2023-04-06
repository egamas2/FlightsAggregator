package clean.core.entities;

import java.time.ZoneId;
import java.util.Comparator;

public interface IExternalQuoteResponse extends Comparable<IExternalQuoteResponse> {
    FlightQuoteResponse adaptResponse(ZoneId zoneId);

    String calculateTotalPrice();
}
