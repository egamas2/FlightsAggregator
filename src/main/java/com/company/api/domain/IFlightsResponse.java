package com.company.api.domain;

import com.company.api.domain.aggregator.FlightAggregatorResponse;

import java.time.ZoneId;

public interface IFlightsResponse<T> extends Comparable<T> {
    FlightAggregatorResponse translate(ZoneId zoneId);
    String calculateTotalPrice();

}
