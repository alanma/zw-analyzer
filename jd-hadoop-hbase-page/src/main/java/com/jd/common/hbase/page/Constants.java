/*
 * Copyright 1998-2013 jd.com All right reserved. This software is the
 * confidential and proprietary information of jd.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jd.com.
 */
package com.jd.common.hbase.page;

/**
 * 类Constants.java的实现描述：
 * @author huanggang 2013-6-5 下午04:43:31
 */
public interface Constants {
    byte[] FOLLOWS_TABLE_NAME = "follows".getBytes();
    byte[] FOLLOWED_BY_TABLE_NAME ="followedBy".getBytes();
    byte[] RELATION_FAMILY="f".getBytes();
    byte[] TO="to".getBytes();
    byte[] FROM="from".getBytes();
    byte[] TO_NAME="to_name".getBytes();
    byte[] FROM_NAME="from_name".getBytes();
    
    public static final String INDEX_PREFIX="index";

}
