package com.gacha.studygacha.controllers;

import com.gacha.studygacha.dtos.requests.CreateStudySessionRequest;
import com.gacha.studygacha.dtos.responses.StudySessionResponse;
import com.gacha.studygacha.exceptions.InvalidStudySessionException;
import com.gacha.studygacha.exceptions.UserNotFoundException;
import com.gacha.studygacha.services.StudySessionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudySessionController.class)
public class StudySessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudySessionService studySessionService;

    @Test
    void shouldCreateStudySession() throws Exception {
        StudySessionResponse response = new StudySessionResponse(
                1L,
                1L,
                Instant.parse("2026-10-12T18:00:00Z"),
                Instant.parse("2026-10-12T19:00:00Z"),
                60,
                5
        );

        when(studySessionService.create(any(CreateStudySessionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/study-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "userId": 1,
                              "startedAt": "2026-10-12T18:00:00Z",
                              "endedAt": "2026-10-12T19:00:00Z"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.durationMinutes").value(60))
                .andExpect(jsonPath("$.ticketsEarned").value(5));

        ArgumentCaptor<CreateStudySessionRequest> captor = ArgumentCaptor.forClass(CreateStudySessionRequest.class);

        verify(studySessionService).create(captor.capture());

        CreateStudySessionRequest capturedRequest = captor.getValue();

        assertEquals(1L, capturedRequest.userId());
        assertEquals(Instant.parse("2026-10-12T18:00:00Z"), capturedRequest.startedAt());
        assertEquals(Instant.parse(("2026-10-12T19:00:00Z")), capturedRequest.endedAt());
    }

    @Test
    void shouldReturnBadRequestWhenUserIdIsMissing() throws Exception {
        mockMvc.perform(post("/study-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "startedAt": "2026-10-12T18:00:00Z",
                          "endedAt": "2026-10-12T19:00:00Z"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

        verify(studySessionService, never()).create(any(CreateStudySessionRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(studySessionService.create(any(CreateStudySessionRequest.class)))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(post("/study-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "userId": 999,
                              "startedAt": "2026-10-12T18:00:00Z",
                              "endedAt": "2026-10-12T19:00:00Z"
                            }
                            """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    @Test
    void shouldReturnBadRequestWhenStudySessionIsInvalid() throws Exception {
        when(studySessionService.create(any(CreateStudySessionRequest.class)))
                .thenThrow(new InvalidStudySessionException("Study session duration must be greater than zero"));

        mockMvc.perform(post("/study-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": 1,
                              "startedAt": "2026-10-12T18:00:00Z",
                              "endedAt": "2026-10-12T17:00:00Z"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("INVALID_STUDY_SESSION"));
    }
}
