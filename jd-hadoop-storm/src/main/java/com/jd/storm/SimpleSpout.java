package com.jd.storm;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SimpleSpout extends BaseRichSpout{

    /**
     *
     */
    private static final long serialVersionUID = -6335251364034714629L;
    private SpoutOutputCollector collector;
    private Jedis jedis;
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("source"));
    }

    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) {
            this.collector=collector;
            jedis=new Jedis("192.168.229.139", 6379);
    }

    public void nextTuple() {
          List<String> messages=jedis.brpop(3600,"msg_queue");
          if(!messages.isEmpty()){
              for (String msg : messages) {
                  collector.emit(new Values(msg));
            }
          }
    }

}