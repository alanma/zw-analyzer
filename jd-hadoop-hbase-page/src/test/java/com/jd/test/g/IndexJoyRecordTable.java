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
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 类IndexJoyRecordTable.java的实现描述：TODO 类实现描述 
 * @author cdtangping 2013-8-15 下午01:11:57
 */
public class IndexJoyRecordTable {
    
    public static final byte[] tableName = "HB_USER_JOY_INDEX".getBytes();
    
    //列族的名字，建议一个表只有一个列族，最多三个。这里该表只有一个。
    public static final byte[] columnFamily = "info".getBytes();
    
    /**
     * cell 即 列名，一个列族下可以有多个列。 这里有2个。
     */
    public static final byte[] qualifier = "rowKeyOfRecord".getBytes();
    
    //此列记录的是 pin 对应的用户的 joy记录 的总条数。
    public static final byte[] qualifier2 = "total".getBytes();
    
    
    /**
     * 先增加用户的joy记录数。
     * 然后根据总记录数和pin生成一个RowKey。
     * @param hTable
     * @param pin
     * @return
     * @throws java.io.IOException
     */
    public static String increasIndexAndGetRowKey(HTableInterface hTable, String pin) throws IOException{
        /**
         * TODO 判空check。
         */
        //先增加记录数。这个是原子操作。
        long newValue = hTable.incrementColumnValue((pin+"#total").getBytes(), columnFamily, qualifier, 1L);
        
        /**
         * 注意：要根据 newValue和pin 生成一个RowKey，这个RowKey必须是字典序递增的。 
         * hbase是前缀匹配。
         * 下面的rowKey生成方式需要验证，验证其是否是递增的。
         */
        return pin + "#" + Bytes.toBytesBinary(newValue+"");
    }
}
