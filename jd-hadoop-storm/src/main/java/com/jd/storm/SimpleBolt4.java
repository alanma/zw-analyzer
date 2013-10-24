package com.jd.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SimpleBolt4  extends BaseBasicBolt{

    /**
     *
     */
    private static final long serialVersionUID = -8025390241512976224L;

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word=input.getString(0);
        if(null!=word&&word.trim()!=""){
            String upper=word.trim().toUpperCase();
            System.out.println(String.format("upper word is:%s", upper));
            collector.emit(new Values(upper));
        }
    }

}