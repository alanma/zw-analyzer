package com.jd.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Hello world!
 */
public class SimpleTopology {
    public static void main(String[] args) throws Exception {
        //实例化topologyBuilder类。
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        //设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。
        topologyBuilder.setSpout("simple-spout", new SimpleSpout(), 1);
        // 设置数据处理节点，并分配并发数。指定该几点接收喷发节点的策略为随机方式。
        topologyBuilder.setBolt("simple-bilt", new SimpleBolt1(), 3).shuffleGrouping("simple-spout");
        topologyBuilder.setBolt("wordcounter", new SimpleBolt2(), 3).fieldsGrouping("simple-bilt", new Fields("info"));
        topologyBuilder.setBolt("word-to-upper", new SimpleBolt4(), 5).shuffleGrouping("simple-spout");
        topologyBuilder.setBolt("store", new SimpleBolt3(), 10).shuffleGrouping("word-to-upper");
        Config config = new Config();
        config.setDebug(true);
        if (null != args && args.length > 0) {
            /*设置该topology在storm集群中要抢占的资源slot数，一个slot对应这supervisor节点上的以个worker进程
             如果你分配的spot数超过了你的物理节点所拥有的worker数目的话，有可能提交不成功，加入你的集群上面已经有了
             一些topology而现在还剩下2个worker资源，如果你在代码里分配4个给你的topology的话，那么这个topology可以提交
             但是提交以后你会发现并没有运行。 而当你kill掉一些topology后释放了一些slot后你的这个topology就会恢复正常运行。
            */
            config.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        } else {
            //使用本地模式运行
            config.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("simple", config, topologyBuilder.createTopology());
        }
    }
}