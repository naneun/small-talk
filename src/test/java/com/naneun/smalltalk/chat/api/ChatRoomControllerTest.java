package com.naneun.smalltalk.chat.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ChatRoomController.class)
@AutoConfigureRestDocs
public class ChatRoomControllerTest {

    final MockMvc mockMvc;
    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ChatRoomControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void 요청받은_채팅방을_생성한다() throws Exception {

        // given
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest("new-chat-room");

        // when
        ResultActions resultActions = mockMvc.perform(post("/chat-rooms")
                .content(objectMapper.writeValueAsString(chatRoomRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("create-new-chat-room",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("The title of the chat room")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("Chat room ID"),
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("The title of the chat room")
                                )
                        )
                );
    }
}
