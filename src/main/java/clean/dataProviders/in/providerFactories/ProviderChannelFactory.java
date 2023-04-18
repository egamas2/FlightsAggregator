package clean.dataProviders.in.providerFactories;

import clean.core.entities.*;
import clean.dataProviders.in.HttpChannel;
import clean.dataProviders.in.hardJet.HardJetChannel;
import clean.dataProviders.in.mongerAir.MongerAirChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

public class ProviderChannelFactory implements IProviderChannelFactory {

    @Autowired
    String hardJetAddress;

    @Autowired
    String mongerAirAddress;

    @Autowired
    ZoneId zoneId;

    @Autowired
    RestTemplate restTemplateExternal;

    /**
     * Should retrieve channel data from a bd
     */
    @Override
    public HttpChannel getChannel(IProvider provider) {
        if (provider.getName().contains("HardJet")) {
            return new HardJetChannel(restTemplateExternal,hardJetAddress,zoneId);
        } else {
            return new MongerAirChannel(restTemplateExternal,mongerAirAddress,zoneId);
        }
    }
}
