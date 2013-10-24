/*
 * Copyright 1998-2013 jd.com All right reserved. This software is the
 * confidential and proprietary information of jd.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jd.com.
 */
package com.jd.common.hbase.page;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;


/**
 * 类PageRegionObserver.java的实现描述：
 *
 * @author cdzhoujianjun 2013-6-16 下午07:49:56
 */
public class PageRegionObserver extends BaseRegionObserver {

    public static final Log logger = LogFactory.getLog(PageRegionObserver.class);

    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s,
                                  List<Result> results, int limit, boolean hasMore) throws IOException {
        return super.preScannerNext(e, s, results, limit, hasMore);
    }

    @Override
    public boolean postScannerNext(ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s,
                                   List<Result> results, int limit, boolean hasMore) throws IOException {
        return super.postScannerNext(e, s, results, limit, hasMore);
    }

    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> env, Put put, WALEdit edit, boolean writeToWAL)
            throws IOException {
        //在插入数据之前先向索引表插入数据
        String regionName = env.getEnvironment().getRegion().getRegionNameAsString();
        String tableName = regionName.substring(0, regionName.indexOf(","));
        if (tableName.startsWith(Constants.INDEX_PREFIX)) {
            return;
        }
        String indexTableName = Constants.INDEX_PREFIX + "_" + tableName;
        HTableInterface hTable = null;
        long rowNum = 0;
        try {
            hTable = env.getEnvironment().getTable(indexTableName.getBytes());
            rowNum = hTable.incrementColumnValue(Bytes.toBytes(-1), "f1".getBytes(), "total".getBytes(), 1);
            Put indexPut = new Put(Bytes.toBytes(rowNum));
            indexPut.add("f1".getBytes(), "rf".getBytes(), put.getRow());
            hTable.put(indexPut);
            hTable.close();
        } catch (Exception e) {
            logger.error("PageRegionObserver prePut " + e.getMessage(), e);
            env.bypass();
        }
        logger.info(tableName + " rowNum is " + rowNum);
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, boolean writeToWAL)
            throws IOException {
        super.postPut(e, put, edit, writeToWAL);


    }

    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit,
                           boolean writeToWAL) throws IOException {
        super.postDelete(e, delete, edit, writeToWAL);
    }

    public static void main(String[] args) {
        String aa = "62.junggtest,,1370420757469.18876c31abbeddec3fcb0fed10e9445b";
        String bb = aa.substring(0, aa.indexOf(","));
        System.out.println(bb);
    }
}
