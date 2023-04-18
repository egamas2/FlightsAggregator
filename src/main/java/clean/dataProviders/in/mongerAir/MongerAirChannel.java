package clean.dataProviders.in.mongerAir;

import clean.core.entities.IExternalQuoteRequest;
import clean.core.entities.IQuoteRequest;
import clean.dataProviders.in.HttpChannel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.List;

public class MongerAirChannel extends HttpChannel<MongerAirQuoteRequest, MongerAirQuoteResponse> {

    private String address;

    private ZoneId zoneId;

    public MongerAirChannel(RestTemplate restTemplateExternal, String hardJetAddress, ZoneId zoneId) {
        super(restTemplateExternal);
        this.address = hardJetAddress;
        this.zoneId = zoneId;
    }

    @Override
    public MongerAirQuoteRequest adaptRequestToExternal(IQuoteRequest request) {
        return MongerAirQuoteRequest.builder()
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .departureDate(request.getDepartureDate())
                .returnDate(request.getReturnDate())
                .passengerCount(request.getNumberOfPassengers()).build();
    }

    @Override
    public ParameterizedTypeReference<List<MongerAirQuoteResponse>> retrieveReturnType() {
        return new ParameterizedTypeReference<List<MongerAirQuoteResponse>>(){};
    }

//**MongerAir API**
//
//**Request**
//
//| Name | Description |
//| ------ | ------ |
//| origin | 3 letter IATA code(eg. LHR, AMS) |
//| destination | 3 letter IATA code(eg. LHR, AMS) |
//| departureDate | ISO_LOCAL_DATE format |
//| returnDate | ISO_LOCAL_DATE format |
//| passengerCount | Number of passengers |

    @Override
    public String formatGetRequest(String address, IExternalQuoteRequest request) {
        return Util.createGetLink(address, request);
    }

    @Override
    protected String getAddress() {
        return address;
    }
}
