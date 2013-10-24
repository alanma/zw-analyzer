package com.logtest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LogTest {
	
private static Log log = LogFactory.getLog("scribe");
	
	public static void main(String[] args) throws InterruptedException {
		log.error("error test!! ");
		log.debug("debug test!! ");
		log.fatal("fatal test!! ");

        Thread.sleep(1000L);
	}
}
