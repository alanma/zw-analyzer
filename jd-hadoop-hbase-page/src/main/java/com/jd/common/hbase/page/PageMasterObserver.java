/*
 * Copyright 1998-2013 jd.com All right reserved. This software is the
 * confidential and proprietary information of jd.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jd.com.
 */
package com.jd.common.hbase.page;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseMasterObserver;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.master.MasterServices;
import org.apache.hadoop.hbase.util.Bytes;




/**
 * 类PageMasterObserver.java的实现描述：
 * @author cdzhoujianjun 2013-6-14 下午02:14:46
 */
public class PageMasterObserver extends BaseMasterObserver {

    public static final Log logger = LogFactory.getLog(PageMasterObserver.class);

    
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#start(org.apache.hadoop.hbase.CoprocessorEnvironment)
     */
    @Override
    public void start(CoprocessorEnvironment ctx) throws IOException {
        // TODO Auto-generated method stub
        super.start(ctx);
        logger.info("PageMasterObserver start");
    }
    
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#stop(org.apache.hadoop.hbase.CoprocessorEnvironment)
     */
    @Override
    public void stop(CoprocessorEnvironment ctx) throws IOException {
        // TODO Auto-generated method stub
        super.stop(ctx);
    }
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#preCreateTable(org.apache.hadoop.hbase.coprocessor.ObserverContext, org.apache.hadoop.hbase.HTableDescriptor, org.apache.hadoop.hbase.HRegionInfo[])
     */
    @Override
    public void preCreateTable(ObserverContext<MasterCoprocessorEnvironment> env, HTableDescriptor desc,
                               HRegionInfo[] regions) throws IOException {
        //在建表之前首先创建索引表
        logger.debug("PageMasterObserver begin preCreateTable");
        try {
            String tableName=desc.getNameAsString();
            if (tableName.startsWith(Constants.INDEX_PREFIX)) {
                return;
            }
            String indexTableName=Constants.INDEX_PREFIX+"_"+tableName;
            MasterServices service= env.getEnvironment().getMasterServices();
            //如果索引表存在则先删除
            if (service.getTableDescriptors().get(indexTableName)!=null) {
                logger.info("begin disable table "+indexTableName);
                service.disableTable(indexTableName.getBytes());
                logger.info(" disable table "+indexTableName+" success");
                service.deleteTable(indexTableName.getBytes());
            }
            HTableDescriptor descriptor=new HTableDescriptor(indexTableName);
            HColumnDescriptor columnDescriptor=new HColumnDescriptor("f1".getBytes());
            descriptor.addFamily(columnDescriptor);
            logger.info("begin create index table "+indexTableName);
            env.getEnvironment().getMasterServices().createTable(descriptor, null);
            logger.info("create index table "+indexTableName+" success");
            //用于保存记录总行数
            HTableInterface htable=env.getEnvironment().getTable(indexTableName.getBytes());
            Put put=new Put(Bytes.toBytes(-1));
            put.add("f1".getBytes(), "total".getBytes(), Bytes.toBytes(0));
            logger.info("begin put total");
            htable.put(put);
            logger.info("pre put total success");
            htable.close();
        } catch (Exception e) {
            logger.error("PageMasterObserver preCreateTable "+e, e);
            env.bypass();
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#postCreateTable(org.apache.hadoop.hbase.coprocessor.ObserverContext, org.apache.hadoop.hbase.HTableDescriptor, org.apache.hadoop.hbase.HRegionInfo[])
     */
    @Override
    public void postCreateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc,
                                HRegionInfo[] regions) throws IOException {
        super.postCreateTable(ctx, desc, regions);
    }
    
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#preDeleteTable(org.apache.hadoop.hbase.coprocessor.ObserverContext, byte[])
     */
    @Override
    public void preDeleteTable(ObserverContext<MasterCoprocessorEnvironment> ctx, byte[] tableName) throws IOException {
        super.preDeleteTable(ctx, tableName);
    }
    
    /* (non-Javadoc)
     * @see org.apache.hadoop.hbase.coprocessor.BaseMasterObserver#postDeleteTable(org.apache.hadoop.hbase.coprocessor.ObserverContext, byte[])
     */
    @Override
    public void postDeleteTable(ObserverContext<MasterCoprocessorEnvironment> env, byte[] tableName) throws IOException {
        //删除表成功之后要删除对应的索引表
        logger.info("PageMasterObserver postDeleteTable");
        MasterServices service= env.getEnvironment().getMasterServices();
        try {
            String indexTableName=Constants.INDEX_PREFIX+"_"+Bytes.toString(tableName);
            if (service.getTableDescriptors().get(indexTableName)!=null) {
                service.disableTable(indexTableName.getBytes());
                service.deleteTable(indexTableName.getBytes());
            }
        } catch (Exception e) {
            logger.error("PageMasterObserver postDeleteTable "+e, e);

        }
    }
}
