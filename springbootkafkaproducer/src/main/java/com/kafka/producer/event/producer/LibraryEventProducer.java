package com.kafka.producer.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Component
public class LibraryEventProducer {

    private static Logger LOG = LoggerFactory.getLogger(LibraryEventProducer.class);

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${event.producer.topic}")
    private String topic;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        ListenableFuture<SendResult<Integer, String>> listenableFuture =
                kafkaTemplate.send(
                        new ProducerRecord(
                                topic,
                                libraryEvent.getLibraryEventId(),
                                objectMapper.writeValueAsString(libraryEvent.getBook())));

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOG.error(ex.getMessage(), ex.getStackTrace());
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handler(result);
            }
        });
    }

    public void sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        SendResult<Integer, String> sendResult =
                kafkaTemplate.sendDefault(libraryEvent.getLibraryEventId(), objectMapper.writeValueAsString(libraryEvent.getBook())).get();
        LOG.info("Synchronous Event sent to topic "
                + sendResult.getRecordMetadata().topic() + " to partition " + sendResult.getRecordMetadata().partition());
    }

    private void handler(SendResult<Integer, String> result) {
        LOG.info("Asynchronous Event sent to topic "
                + result.getRecordMetadata().topic() + " to partition " + result.getRecordMetadata().partition());
    }

}
