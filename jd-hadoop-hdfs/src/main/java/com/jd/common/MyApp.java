package com.jd.common;



public class MyApp {
  public static void main(String[] args) {
    MyRpcClientFacade client = new MyRpcClientFacade();
    // Initialize client with the remote Flume agent's host and port
    client.init("host.example.org", 41414);

    // Send 10 events to the remote Flume agent. That agent should be
    // configured to listen with an AvroSource.
    String sampleData = "Hello Flume!";
    for (int i = 0; i < 10; i++) {
      client.sendDataToFlume(sampleData);
    }

    client.cleanUp();
  }
}
