package org.apache.flume.thrift;



import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: zhangwei
 * Date: 2013-9-30
 * Time: 17:48:40
 * To change this template use File | Settings | File Templates.
 */
public class ClientFactory {
        public static LocalClient getClient(String ip, int port) throws TTransportException, IOException, org.apache.thrift.transport.TTransportException {
        TSocket transport = new TSocket(ip, port);

        TProtocol protocol = new TBinaryProtocol(transport);
        transport.open();
        LocalClient client = new LocalClient(protocol, port, transport);
        return client;
    }


    public static void main(String[] args) throws IOException, TException {
        LocalClient client = ClientFactory.getClient("192.168.229.139", 4141);
        ByteBuffer bb = ByteBuffer.wrap("Hello".getBytes());
        client.append("abc", bb);
        System.out.println("ok");

        System.out.println(new String(client.get("aaa").array()));
    }
}
