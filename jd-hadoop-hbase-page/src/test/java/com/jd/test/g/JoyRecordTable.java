/*
 * Copyright 1998-2012 360buy.com All right reserved. This software is the
 * confidential and proprietary information of 360buy.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with 360buy.com.
 */
package com.jd.test.g;

/**
 * 类JoyRecordTable.java的实现描述：
 * 该表是存储数据的主表，其结构如此类。 
 * @author cdtangping 2013-8-15 下午12:56:11
 */
public class JoyRecordTable {
    
    public static final byte[] tableName = "HB_USER_JOY".getBytes();
    
    //列族的名字，建议一个表只有一个列族，最多三个。这里该表只有一个。
    public static final byte[] columnFamily = "info".getBytes();
    
    //cell 即 列名，一个列族下可以有多个列。 这里只有一个。
    public static final byte[] qualifier = "value".getBytes();
    
    
    /**
     * 生成一个该表的RowKey。
     * 
     * 形如：pin#joyType#id
     * @param pin
     * @param joyType
     * @param id
     * @return
     */
    public static String getRowKey(String pin, String joyType, String id){
        /**
         * TODO 判空check。
         */
        return pin + "#" + joyType + "#" + id;
    }
}
