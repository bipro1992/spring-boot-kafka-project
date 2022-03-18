package com.kafka.consumer.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LibraryEventConsumer {

    private static Logger LOG = LoggerFactory.getLogger(LibraryEventConsumer.class);

    @KafkaListener(topics = "${event.consumer.topic}")
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord){
        LOG.info("Data received from --> " +
                "Topic: "+consumerRecord.topic()+", " +
                "Partition: "+consumerRecord.partition()+"," +
                "Key: "+consumerRecord.key()+", " +
                "Value: "+consumerRecord.value());
    }
}
