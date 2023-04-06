package clean.core.entities;

import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public interface IChannel<T extends IExternalQuoteRequest, U extends IExternalQuoteResponse> {

    T adaptRequestToExternal(IQuoteRequest request);

    List<U> retrieveFlightQuotes(T request, ParameterizedTypeReference<List<U>> returnType);

    ParameterizedTypeReference<List<U>> retrieveReturnType();
}
