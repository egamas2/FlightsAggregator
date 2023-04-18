package clean.dataProviders.out.config;

import clean.core.entities.IProviderChannelFactory;
import clean.core.entities.IProviderFactory;
import clean.core.useCases.FlightsAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightsAggregatorImplementor extends FlightsAggregator {

    public FlightsAggregatorImplementor(@Autowired IProviderFactory providerFactory, @Autowired IProviderChannelFactory providerChannelFactory) {
        super(providerFactory, providerChannelFactory);
    }



}
