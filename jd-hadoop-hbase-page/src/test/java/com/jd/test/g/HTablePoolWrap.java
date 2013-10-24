/*
 * Copyright 1998-2012 360buy.com All right reserved. This software is the
 * confidential and proprietary information of 360buy.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with 360buy.com.
 */
package com.jd.test.g;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

/**
 * 类HTablePoolWrap.java的实现描述：
 * 建议用HTablePool来获取HTable的实例，客户端建议只使用一个HTablePool来操作一个集群中的表。
 * @author cdtangping 2013-8-15 下午12:44:55
 */
public class HTablePoolWrap {
    private String zkIps;
    private String zkPort;
    private HTablePool hTablePool;
    
    public HTablePoolWrap(String zkIps,String zkPort){
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zkIps);
        configuration.set("hbase.zookeeper.property.clientPort", zkPort);
        
        // 1000 指的是pool中的某个表的实例个数最大值。当超出这个值时，新获取的HTable实例将不会放在pool中。
        hTablePool = new HTablePool(configuration, 1000);
    }
    
    public HTableInterface getHTable(byte[] tableName){
        return hTablePool.getTable(tableName);
    }
    
    public HTableInterface getHTable(String tableName){
        return getHTable(tableName.getBytes());
    }
    
    /**
     * HTablePool的生命周期应该是和应用的生命周期相同，所以这个方法只有应用退出的时候调用。
     * @throws java.io.IOException
     */
    public void closePool() throws IOException{
        hTablePool.close();
    }
}
