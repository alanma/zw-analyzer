/*
 * Copyright 1998-2013 jd.com All right reserved. This software is the confidential and proprietary information of
 * jd.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with jd.com.
 */
package com.jd.test.g;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jd.common.hbase.page.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;


/**
 * 类TestPageFilter.java的实现描述：
 * 
 * @author cdzhoujianjun 2013-6-13 下午04:43:39
 */
public class TestPageFilter {

    public static Configuration configuration;
    public static HBaseAdmin    hBaseAdmin;
    public static HTablePool    pool;
    public static byte[]        POSTFIX = "0".getBytes();
    static {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node34,node38,node44");
     /*   configuration.set("hbase.master", "10.28.171.37:600000");*/
        try {
            hBaseAdmin = new HBaseAdmin(configuration);
            pool = new HTablePool(configuration, 1000);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String tableName="user_JoyAccount";
        //分页查询
        queryByPage(tableName, 10, 2);
    }

    public static void testFilter(String tableName, Scan scan) throws IOException {

        HTableInterface table = pool.getTable(tableName);
        ResultScanner scanner = table.getScanner(scan);
        int num = 0;
        Result res = null;
        while ((res = scanner.next()) != null) {
            System.out.println(res);
            num++;
        }
        System.out.println("page:" + num);
        System.out.println("*************************************************");
    }
    
    
    /**
     * 分页查询
     * @param tableName
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws Exception 
     */
    public static Result[] queryByPage(String tableName,int pageSize,int pageIndex) throws Exception{
        if (pageSize<0) {
            throw new Exception("pageSize can't less than zero");
        }
        if (pageIndex<0) {
            throw new Exception("pageIndex can't less than zero");
        }
        Scan scan=new Scan();
        
        List<Get>gets=null;
        
        String indexTableName= Constants.INDEX_PREFIX+tableName;
        
        HTableInterface indexTable = pool.getTable(indexTableName);
        scan.setStartRow(Bytes.toBytes((pageIndex-1)*pageSize+1));
        scan.setStopRow(Bytes.toBytes(pageIndex*pageSize));
        //scan索引表获取rowKeys
        List<byte[]> rows=getRowKeysFromIndexTable(scan,indexTable);
        gets=generageGets(rows);
        
        HTableInterface table = pool.getTable(tableName);
        Result[] res=table.get(gets);
        return res;
    }
    /**
     * @param rows
     * @return
     */
    private static List<Get> generageGets(List<byte[]> rows) {
        if (rows==null||rows.size()==0) {
            return null;
        }
        List<Get>gets=new ArrayList<Get>();
        for(int i=0;i<rows.size();i++){
            Get get=new Get(rows.get(i));
            gets.add(get);
        }
        return gets;
    }

    /**
     * @param scan
     * @param indexTable
     * @throws java.io.IOException
     */
    private static List<byte[]> getRowKeysFromIndexTable(Scan scan, HTableInterface indexTable) throws IOException {
        List<byte[]> rows=new ArrayList<byte[]>();
        ResultScanner scanner=indexTable.getScanner(scan);
        Result res=null;
        while((res=scanner.next())!=null){
            byte[] row=res.getValue("f1".getBytes(), "rf".getBytes());
            rows.add(row);
        }
        return rows;
    }

    public static void pageFilter(String tableName) throws IOException {
        HTableInterface table = pool.getTable(tableName);
        PageFilter filter = new PageFilter(2);
        int totalRows = 0;
        byte[] lastRow = null;
        while (true) {
            Scan scan = new Scan();
            scan.setFilter(filter);
            if (lastRow != null) {
                byte[] startRow = Bytes.add(lastRow, POSTFIX);
                System.out.println("start row:" + Bytes.toStringBinary(startRow));
                scan.setStartRow(startRow);
            }
            ResultScanner scanner=table.getScanner(scan);
            int localRows=0;
            Result res=null;
            while((res=scanner.next())!=null){
                System.out.println(localRows++ +":"+res);
                totalRows++;
                lastRow=res.getRow();
            }
            scanner.close();
            if (localRows==0) {
                break;
            }
        }

    }
    
    
}
