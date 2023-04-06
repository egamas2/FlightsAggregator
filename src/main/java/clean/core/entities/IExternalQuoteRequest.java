package clean.core.entities;

public interface IExternalQuoteRequest {
    String getFrom();

    String getTo();

    String getOutboundDate();

    String getInboundDate();

    int getNumberOfAdults();
}
