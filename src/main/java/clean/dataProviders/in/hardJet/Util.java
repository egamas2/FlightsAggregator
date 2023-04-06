package clean.dataProviders.in.hardJet;

import clean.core.entities.IExternalQuoteRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class Util {
    public static String createGetLink(String address, IExternalQuoteRequest request) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address);
        if (!StringUtils.isEmpty(request.getFrom())) {
            builder.queryParam("from", request.getFrom());
        }
        if (!StringUtils.isEmpty(request.getTo())) {
            builder.queryParam("to", request.getTo());
        }
        if (!StringUtils.isEmpty(request.getOutboundDate())) {
            builder.queryParam("outboundDate", request.getOutboundDate());
        }
        if (!StringUtils.isEmpty(request.getInboundDate())) {
            builder.queryParam("inboundDate", request.getInboundDate());
        }
        builder.queryParam("numberOfAdults", request.getNumberOfAdults());
        return builder.toUriString();
    }
}
