package com.kafka.domain;

public record Book(
        Integer bookId,
        String bookName,
        String bookAuthor
) {
    // No need to explicitly define constructors, equals(), hashCode(), or toString()
}

