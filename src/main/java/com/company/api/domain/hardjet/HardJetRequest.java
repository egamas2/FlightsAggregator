package com.company.api.domain.hardjet;

import com.company.api.domain.IFlightsRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class HardJetRequest implements IFlightsRequest {

    private String from;
    private String to;
    private String outboundDate; //ISO_LOCAL_DATE
    private String inboundDate; //ISO_LOCAL_DATE
    private int numberOfAdults;

    @Override
    public String formatGetRequest(String baseUri) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri);
        if (!StringUtils.isEmpty(from)) {
            builder.queryParam("from", from);
        }
        if (!StringUtils.isEmpty(to)) {
            builder.queryParam("to", to);
        }
        if (!StringUtils.isEmpty(outboundDate)) {
            builder.queryParam("outboundDate", outboundDate);
        }
        if (!StringUtils.isEmpty(inboundDate)) {
            builder.queryParam("inboundDate", inboundDate);
        }
        builder.queryParam("numberOfAdults", numberOfAdults);
        return builder.toUriString();
    }
}
