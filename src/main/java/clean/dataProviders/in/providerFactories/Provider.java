package clean.dataProviders.in.providerFactories;

import clean.core.entities.IProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Provider implements IProvider {
    String name;
}
