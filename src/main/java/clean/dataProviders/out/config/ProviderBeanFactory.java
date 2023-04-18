package clean.dataProviders.out.config;

import clean.core.entities.IProviderChannelFactory;
import clean.core.entities.IProviderFactory;
import clean.dataProviders.in.providerFactories.ProviderChannelFactory;
import clean.dataProviders.in.providerFactories.ProviderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

public class ProviderBeanFactory {
    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(timeZone);
    }

    @Bean
    public RestTemplate restTemplateExternal() {
        return new RestTemplate();
    }//For the external services

    @Bean
    public String hardJetAddress() {
        return hardJetAddress;
    }

    @Bean
    public String mongerAirAddress() {
        return mongerAirAddress;
    }

    @Bean
    public IProviderChannelFactory providerChannelFactory() {
        return new ProviderChannelFactory();
    }

    @Bean
    public IProviderFactory providerFactory() {
        return new ProviderFactory();
    }

    @Value("${hardJet.http.address}")
    private String hardJetAddress;

    @Value("${mongerAir.http.address}")
    private String mongerAirAddress;

    @Value("${flightAggregator.timeZone}")
    private String timeZone;


}
