/*
 * Copyright 1998-2012 360buy.com All right reserved. This software is the
 * confidential and proprietary information of 360buy.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with 360buy.com.
 */
package com.jd.test.g;

import java.io.IOException;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;

/**
 * 类PageQuery.java的实现描述：TODO 类实现描述 
 * @author cdtangping 2013-8-15 下午01:44:42
 */
public class PageQuery {
    public static final HTablePoolWrap tablePool = new HTablePoolWrap("node31,node38","2181");
    
    /**
     * 插入记录。
     * @param pin
     * @param joyType
     * @param id
     * @param value
     * @throws java.io.IOException
     */
    public static void put(String pin, String joyType, String id, String value){
        //TODO 判空。
        
        
        HTableInterface table = tablePool.getHTable(JoyRecordTable.tableName);
        HTableInterface tableIndex = tablePool.getHTable(IndexJoyRecordTable.tableName);
        
        String rowKey = JoyRecordTable.getRowKey(pin, joyType, id);
        /**
         * 先操作索引表。
         */
        try {
            String rowKeyOfIndex = IndexJoyRecordTable.increasIndexAndGetRowKey(tableIndex, pin);
            Put indexPut = new Put(rowKeyOfIndex.getBytes());
            
            //获取到 有编号的 rowKeyOfIndex后，将其插入索引表，值为数据表的 对应记录的RowKey。
            indexPut.add(IndexJoyRecordTable.columnFamily, IndexJoyRecordTable.qualifier, rowKey.getBytes());
            tableIndex.put(indexPut);
        }catch(IOException e) {
            //索引表操作不成功，抛异常。
            throw new RuntimeException(e);
        }
        
        /**
         * 再操作数据表。
         */
        Put put = new Put(rowKey.getBytes());
        put.add(JoyRecordTable.columnFamily, JoyRecordTable.qualifier, value.getBytes());
        try {
            table.put(put);
        } catch (IOException e) {
            //如果是数据表插入数据不成功，抛异常。
            throw new RuntimeException(e);
        }
        
        
    }
}
