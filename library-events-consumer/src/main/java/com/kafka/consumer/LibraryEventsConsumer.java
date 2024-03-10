package com.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryEventsConsumer {

    @Autowired
    private LibraryEventService libraryEventService;

    @KafkaListener(topics = {"library-events"} )
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("consumerRecords : {}", consumerRecord);
        libraryEventService.processLibraryEvent(consumerRecord);
    }
}