package com.kafka.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.LibraryEvent;
import com.kafka.util.TestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LibraryEventsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPostLibraryEvent() throws Exception {
        //given -precondition or setup
        LibraryEvent libraryEvent = TestUtil.libraryEventRecord();// Create a sample LibraryEvent
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);// Convert the LibraryEvent to JSON

        //when -action or behaviour that we are going to test
        // Perform a POST request to /v1/libraryevent endpoint with the LibraryEvent JSON
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libraryEventId", CoreMatchers.is(libraryEvent.libraryEventId())))
                .andExpect(jsonPath("$.book.bookName", CoreMatchers.is("spring for kafka")));
    }
}


/**
 * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT):
 * This annotation is used to specify that the test should load the full application context
 * and start an embedded web server with a random port.
 * @AutoConfigureMockMvc: This annotation is used to automatically configure the MockMvc instance for use in the tests.
 * It allows you to send HTTP requests and verify the responses without the overhead of running a real HTTP server.
 */