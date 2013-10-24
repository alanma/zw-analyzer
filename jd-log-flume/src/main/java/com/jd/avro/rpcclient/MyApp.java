package com.jd.avro.rpcclient;

public class MyApp {
  public static void main(String[] args) {
    MyRpcClientFacade client = new MyRpcClientFacade();
    // Initialize client with the remote Flume agent's host and port
    client.init("192.168.229.138", 41414);

    // Send 10 events to the remote Flume agent. That agent should be
    // configured to listen with an AvroSource.
    String sampleData = "Hello Flume111111!";

    for (int i = 0; i < 10; i++) {
      client.sendDataToFlume(sampleData);

    }

    client.cleanUp();
  }
}