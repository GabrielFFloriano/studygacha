package com.gacha.studygacha.controllers;

import com.gacha.studygacha.dtos.requests.CreateStudySessionRequest;
import com.gacha.studygacha.dtos.responses.StudySessionResponse;
import com.gacha.studygacha.services.StudySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study-sessions")
@RequiredArgsConstructor
public class StudySessionController {
    private final StudySessionService studySessionService;

    @PostMapping
    public ResponseEntity<StudySessionResponse> create(@Valid @RequestBody CreateStudySessionRequest request){
        StudySessionResponse response = studySessionService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
