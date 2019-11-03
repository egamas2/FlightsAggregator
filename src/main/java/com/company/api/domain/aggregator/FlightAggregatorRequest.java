package com.company.api.domain.aggregator;

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
public class FlightAggregatorRequest implements IFlightsRequest {

    private String origin;
    private String destination;
    private String departureDate; //ISO_LOCAL_DATE
    private String returnDate; //ISO_LOCAL_DATE
    private int numberOfPassengers;

    @Override
    public String formatGetRequest(String baseUri) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri);
        if (!StringUtils.isEmpty(origin)) {
            builder.queryParam("origin", origin);
        }
        if (!StringUtils.isEmpty(destination)) {
            builder.queryParam("destination", destination);
        }
        if (!StringUtils.isEmpty(departureDate)) {
            builder.queryParam("departureDate", departureDate);
        }
        if (!StringUtils.isEmpty(returnDate)) {
            builder.queryParam("returnDate", returnDate);
        }
        builder.queryParam("numberOfPassengers", numberOfPassengers);
        return builder.toUriString();
    }
}