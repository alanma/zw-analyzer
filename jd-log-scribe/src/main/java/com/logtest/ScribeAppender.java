package com.logtest;

import net.scribe.LogEntry;
import net.scribe.scribe;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.util.List;
import java.util.ArrayList;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.IOException;

/**
 * 继承WriterAppender 
 * 实现了scribe 服务器的链接和日志的发送。
 * @author ninja
 */
public class ScribeAppender extends WriterAppender {

	private String hostname;
	private String scribeHost;
	private int scribePort;
	private String scribeCategory;
	private String encoding;

	private List<LogEntry> logEntries;

	private scribe.Client client;
	private TFramedTransport transport;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getScribeHost() {
		return scribeHost;
	}

	public void setScribeHost(String scribeHost) {
		this.scribeHost = scribeHost;
	}

	public int getScribePort() {
		return scribePort;
	}

	public void setScribePort(int scribePort) {
		this.scribePort = scribePort;
	}

	public String getScribeCategory() {
		return scribeCategory;
	}

	public void setScribeCategory(String scribeCategory) {
		this.scribeCategory = scribeCategory;
	}
	

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/*
	 * Activates this Appender by opening a transport to the Scribe server.
	 */
	@Override
	public void activateOptions() {
		try {
			synchronized (this) {
				if (hostname == null) {
					try {
						hostname = InetAddress.getLocalHost()
								.getCanonicalHostName();
					} catch (UnknownHostException e) {
						// can't get hostname
					}
				}
		System.out.println(scribeHost + scribePort + scribeCategory + encoding);
				// Thrift boilerplate code
				logEntries = new ArrayList<LogEntry>(1);
				TSocket sock = new TSocket(new Socket(scribeHost, scribePort));
				transport = new TFramedTransport(sock);
				TBinaryProtocol protocol = new TBinaryProtocol(transport,
						false, false);
				client = new scribe.Client(protocol, protocol);
				// This is commented out because it was throwing Exceptions for
				// no good reason.
				// transport.open();
			}
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Appends a log message to Scribe
	 */
	@Override
	public void append(LoggingEvent event) {
		synchronized (this) {
			try {
				String message = String.format("%s %s", hostname, layout
						.format(event));
				LogEntry entry = new LogEntry(scribeCategory, message);
				logEntries.add(entry);
				client.Log(logEntries);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				logEntries.clear();
			}
		}
	}

	@Override
	public void close() {
		if (transport != null) {
			transport.close();
		}
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}
}