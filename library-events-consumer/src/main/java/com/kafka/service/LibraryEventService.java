package com.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.entity.LibraryEvent;
import com.kafka.repository.LibraryEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LibraryEventService {

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void processLibraryEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);//to deserialize a JSON string obtained from a Kafka ConsumerRecord into a LibraryEvent object
        log.info("libraryEvent: {}", libraryEvent);

        switch (libraryEvent.getLibraryEventType()){
            case NEW:
                //save
                save(libraryEvent);
                break;
            case UPDATE:
               // validate the libraryEvent
                validate(libraryEvent);
                save(libraryEvent);
            default:
                log.info("Invalid Library Event type");
        }
    }

    private void validate(LibraryEvent libraryEvent) {
        if (libraryEvent.getLibraryEventId()==null){
            throw new IllegalArgumentException("library event id is missing");
        }

        Optional<LibraryEvent> libraryEventOptional = libraryEventsRepository.findById(libraryEvent.getLibraryEventId());
        if (!libraryEventOptional.isPresent()){
            throw new IllegalArgumentException("not a valid library event");
        }
        log.info("validation is successful for library event : {}",libraryEventOptional.get());

    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully Persisted the library Event {} ", libraryEvent);
    }
}
