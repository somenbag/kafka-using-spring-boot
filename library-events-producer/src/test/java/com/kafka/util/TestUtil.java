package com.kafka.util;

import com.kafka.domain.Book;
import com.kafka.domain.LibraryEvent;
import com.kafka.domain.LibraryEventType;

public class TestUtil {

    public static Book bookRecords() {
        return new Book(123, "spring for kafka", "somen");
    }

    public static Book bookRecordsWithInvalidValues() {
        return new Book(null, "", "somen");
    }

    public static LibraryEvent libraryEventRecord() {
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecords());
    }

    public static LibraryEvent libraryEventRecordWithInvalidId(){
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecordsWithInvalidValues());
    }
}
