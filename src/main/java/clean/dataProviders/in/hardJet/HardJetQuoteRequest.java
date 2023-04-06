package clean.dataProviders.in.hardJet;

import clean.core.entities.IExternalQuoteRequest;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardJetQuoteRequest implements IExternalQuoteRequest {
    private String from;
    private String to;
    private String outboundDate; //ISO_LOCAL_DATE
    private String inboundDate; //ISO_LOCAL_DATE
    private int numberOfAdults;
}