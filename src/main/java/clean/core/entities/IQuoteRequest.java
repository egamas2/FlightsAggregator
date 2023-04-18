package clean.core.entities;


public interface IQuoteRequest {
    String getOrigin();

    String getDestination();

    String getDepartureDate();//ISO_LOCAL_DATE

    String getReturnDate(); //ISO_LOCAL_DATE

    int getNumberOfPassengers();

}
