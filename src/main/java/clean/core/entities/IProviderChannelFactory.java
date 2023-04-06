package clean.core.entities;

public interface IProviderChannelFactory {
    IChannel<IExternalQuoteRequest,IExternalQuoteResponse> getChannel(IProvider provider);
}
