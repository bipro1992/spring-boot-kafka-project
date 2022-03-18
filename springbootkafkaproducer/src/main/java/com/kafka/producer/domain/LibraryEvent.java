package com.kafka.producer.domain;

public class LibraryEvent {

    private Integer libraryEventId;
    private Book book;
    private LibraryEventType eventType;

    public LibraryEvent() {
    }

    public Integer getLibraryEventId() {
        return libraryEventId;
    }

    public void setLibraryEventId(Integer libraryEventId) {
        this.libraryEventId = libraryEventId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryEventType getEventType() {
        return eventType;
    }

    public void setEventType(LibraryEventType eventType) {
        this.eventType = eventType;
    }
}
