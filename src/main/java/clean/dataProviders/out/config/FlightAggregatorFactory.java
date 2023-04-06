package clean.dataProviders.out.config;

import clean.core.entities.IProviderChannelFactory;
import clean.core.entities.IProviderFactory;
import clean.core.useCases.FlightsAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class FlightAggregatorFactory {
    @Autowired
    IProviderFactory providerFactory;
    @Autowired
    IProviderChannelFactory providerChannelFactory;

    @Bean
    public FlightsAggregator flightsAggregator() {
        return new FlightsAggregatorImplementor(providerFactory, providerChannelFactory);
    }
}
