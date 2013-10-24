package com.logtest;

import org.apache.log4j.AsyncAppender;
/**
 * log4j 的scribe appender
 * 用ScribeAppender 类连接scribe服务器，并把日志写如scribe
 * @author ninja
 */
public class AsyncScribeAppender extends AsyncAppender {

	private String hostname;
	private String scribeHost;
	private int scribePort;
	private String scribeCategory;
	private String encoading;

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

	public String getEncoading() {
		return encoading;
	}

	public void setEncoading(String encoading) {
		this.encoading = encoading;
	}

	@Override
	public void activateOptions() {
		super.activateOptions();
		synchronized (this) {
			ScribeAppender scribeAppender = new ScribeAppender();
			scribeAppender.setLayout(getLayout());
			scribeAppender.setHostname(getHostname());
			scribeAppender.setScribeHost(getScribeHost());
			scribeAppender.setScribePort(getScribePort());
			scribeAppender.setScribeCategory(getScribeCategory());
			scribeAppender.setEncoding(getEncoading());
			scribeAppender.activateOptions();
			addAppender(scribeAppender);
		}
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}
     public static void main(String [] arg){

         System.out.println(1345603036.23 % 3600);
     }
}