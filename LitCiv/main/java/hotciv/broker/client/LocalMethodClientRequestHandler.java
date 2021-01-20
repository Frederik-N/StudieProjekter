package hotciv.broker.client;

import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import frds.broker.RequestObject;

public class LocalMethodClientRequestHandler implements ClientRequestHandler {
    private final Invoker invoker;

    public LocalMethodClientRequestHandler(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public ReplyObject sendToServer(RequestObject requestObject) {
        ReplyObject reply = invoker.handleRequest(requestObject.getObjectId(), requestObject.getOperationName(), requestObject.getPayload());
        return reply;
    }

    @Override
    public void setServer(String hostname, int port) {
    }

    @Override
    public void close() {

    }
}
