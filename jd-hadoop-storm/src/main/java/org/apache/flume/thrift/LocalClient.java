package org.apache.flume.thrift;


import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @author xiemalin
 */
public class LocalClient extends ThriftSourceProtocol.Client {
    private TTransport transport;

    public LocalClient(TProtocol prot, int port, TTransport ftransport) throws TTransportException {
        super(prot);
        this.transport = ftransport;
    }

    public void close() {
        this.transport.close();
    }


}