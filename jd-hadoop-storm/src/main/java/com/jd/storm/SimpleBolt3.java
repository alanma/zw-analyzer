package com.jd.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class SimpleBolt3 extends BaseBasicBolt{

    /**
     *
     */
    private static final long serialVersionUID = 9140971206523366543L;

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word=input.getString(0);
        StoreDatabase.insertRow(word);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
    }

}