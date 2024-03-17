package com.kafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.LibraryEvent;
import com.kafka.producer.LibraryEventsProducer;
import com.kafka.util.TestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryEventsController.class)
public class LibraryEventsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryEventsProducer libraryEventsProducer;

    @Test
    public void testPostLibraryEvent() throws Exception {

        //given -precondition or setup
        LibraryEvent libraryEvent = TestUtil.libraryEventRecord();// Create a sample LibraryEvent
        when(libraryEventsProducer.sendLibraryEvent(any())).thenReturn(null);// Mock the behavior of libraryEventsProducer.sendLibraryEvent(libraryEvent)

        //when -action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                .content(objectMapper.writeValueAsString(libraryEvent))
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libraryEventId", CoreMatchers.is(libraryEvent.libraryEventId())))
                .andExpect(jsonPath("$.book.bookName", CoreMatchers.is("spring for kafka")));
        verify(libraryEventsProducer, times(1)).sendLibraryEvent(libraryEvent);
    }

    @Test
    public void testPostLibraryEvent_invalidValues() throws Exception {

        //given -precondition or setup
        LibraryEvent libraryEvent = TestUtil.libraryEventRecordWithInvalidId();// Create a sample LibraryEvent
        when(libraryEventsProducer.sendLibraryEvent(any())).thenReturn(null);// Mock the behavior of libraryEventsProducer.sendLibraryEvent(libraryEvent)
        String expectedErrorMessage = "book.bookId - must not be null, book.bookName - must not be blank";
        //when -action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                .content(objectMapper.writeValueAsString(libraryEvent))
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

}

/**
 * @WebMvcTest(LibraryEventsController.class) : only load the components related to LibraryEventsController and
 * its dependencies required for handling HTTP requests.
 */