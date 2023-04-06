package clean.dataProviders.in.hardJet;

import clean.core.entities.IExternalQuoteRequest;
import clean.core.entities.IQuoteRequest;
import clean.dataProviders.in.HttpChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZoneId;
import java.util.List;

@Service
public class HardJetChannel extends HttpChannel<HardJetQuoteRequest, HardJetQuoteResponse> {

    private String address;

    private ZoneId zoneId;

    Logger log = LoggerFactory.getLogger(HardJetChannel.class);

    public HardJetChannel(RestTemplate restTemplateExternal, String hardJetAddress, ZoneId zoneId) {
        super(restTemplateExternal);
        this.address = hardJetAddress;
        this.zoneId = zoneId;
    }

    @Override
    protected String getAddress() {
        return address;
    }

    @Override
    public HardJetQuoteRequest adaptRequestToExternal(IQuoteRequest request) {
        return HardJetQuoteRequest.builder()
                .from(request.getOrigin())
                .to(request.getDestination())
                .inboundDate(request.getReturnDate())
                .outboundDate(request.getDepartureDate())
                .numberOfAdults(request.getNumberOfPassengers())
                .build();
    }

    @Override
    public ParameterizedTypeReference<List<HardJetQuoteResponse>> retrieveReturnType() {
        return new ParameterizedTypeReference<List<HardJetQuoteResponse>>(){};
    }

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
    @Override
    public String formatGetRequest(String address, IExternalQuoteRequest request) {
        return Util.createGetLink(address, request);
    }

}
