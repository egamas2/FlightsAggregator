package clean.dataProviders.in.providerFactories;

import clean.core.entities.IProvider;
import clean.core.entities.IProviderFactory;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ProviderFactory implements IProviderFactory {
    @Override
    public List<IProvider> listAllProviders() {
        return List.of(new Provider("HardJet"), new Provider("MongerAir"));
    }
}
