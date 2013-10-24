package org.apache.flume.thrift;

import com.xmatthew.thrift.demo.DemoService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangwei
 * Date: 2013-9-30
 * Time: 17:25:22
 * To change this template use File | Settings | File Templates.
 */
public class Server implements ThriftSourceProtocol.Iface {

    private final int port;

    private final TThreadPoolServer tr_server;

    public Server(int _port) throws TTransportException {
        this.port = _port;
        TBinaryProtocol.Factory protoFactory = new TBinaryProtocol.Factory(true, true);
        TServerTransport serverTransport = new TServerSocket(port);
        ThriftSourceProtocol.Processor processor = new ThriftSourceProtocol.Processor(this);
        tr_server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor)
                .protocolFactory(protoFactory));
    }

    public void run() {
        tr_server.serve();
    }

    public synchronized void close() {
        tr_server.stop();
    }



    public static void main(String[] args) throws TTransportException {
        Server server = new Server(4141);
        server.run();
    }

    @Override
    public Status append(ThriftFlumeEvent event) throws TException {
        System.out.println("111");

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Status appendBatch(List<ThriftFlumeEvent> events) throws TException {
                System.out.println("2222");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}