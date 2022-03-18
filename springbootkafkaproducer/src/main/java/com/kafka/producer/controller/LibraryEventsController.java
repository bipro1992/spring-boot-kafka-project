package com.kafka.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.producer.domain.LibraryEvent;
import com.kafka.producer.domain.LibraryEventType;
import com.kafka.producer.event.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
 * REST API class for sending library events to kafka consumer to add or update via kafka producer
 *
 * */

@RestController
public class LibraryEventsController {

    @Autowired
    private LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryRecord/create")
    public ResponseEntity<?> postLibraryEvent(@RequestBody LibraryEvent libraryEvent,
                                              @RequestParam String isSyncRequired) throws JsonProcessingException {
        try {
            libraryEvent.setEventType(LibraryEventType.NEW);
            if (Boolean.valueOf(isSyncRequired)) {
                libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
            } else {
                libraryEventProducer.sendLibraryEvent(libraryEvent);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryRecord/update")
    public ResponseEntity<?> update(@RequestBody LibraryEvent libraryEvent,
                                    @RequestParam String isSyncRequired) {
        try {
            Optional<Integer> libraryEventIdCheck = Optional.ofNullable(libraryEvent.getLibraryEventId());
            if (libraryEventIdCheck.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Library event Id cannot be empty or null.");
            }
            libraryEvent.setEventType(LibraryEventType.UPDATE);
            if (Boolean.valueOf(isSyncRequired)) {
                libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
            } else {
                libraryEventProducer.sendLibraryEvent(libraryEvent);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }


}
