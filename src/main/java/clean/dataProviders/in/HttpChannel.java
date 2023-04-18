package clean.dataProviders.in;

import clean.core.entities.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class HttpChannel<T extends IExternalQuoteRequest, U extends IExternalQuoteResponse>
        implements IChannel<T, U> {

    RestTemplate restTemplate;

    public HttpChannel(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<U> retrieveFlightQuotes(T request, ParameterizedTypeReference<List<U>> returnType) {
        return restTemplate.exchange(
                formatGetRequest(getAddress(), request),
                HttpMethod.GET,
                null,
                returnType).getBody();
    }

    protected abstract String getAddress();

    //**HardJet API**
    //
    //**Request**
    //
    //| Name | Description |
    //| ------ | ------ |
    //| from | 3 letter IATA code(eg. LHR, AMS) |
    //| to | 3 letter IATA code(eg. LHR, AMS) |
    //| outboundDate |ISO_LOCAL_DATE format |
    //| inboundDate | ISO_LOCAL_DATE format |
    //| numberOfAdults | Number of passengers |
    public abstract String formatGetRequest(String address, IExternalQuoteRequest request);
}
