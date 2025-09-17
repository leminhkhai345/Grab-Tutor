package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.SubjectRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    SubjectService subjectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<?> createSubject(@RequestBody @Valid SubjectRequest subjectRequest) {
        return ApiResponse.builder()
                .data(subjectService.createSubject(subjectRequest))
                .message("Subject created successfully")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{id}")
    public ApiResponse<?> updateSubject(@PathVariable String id, @RequestBody @Valid SubjectRequest subjectRequest) {
        return ApiResponse.builder()
                .data(subjectService.updateSubject(id, subjectRequest))
                .message("Subject updated successfully")
                .build();
    }


    @GetMapping("{id}")
    public ApiResponse<?> getSubjectById(@PathVariable String id) {
        return ApiResponse.builder()
                .data(subjectService.getSubjectById(id))
                .message("Subject fetched successfully")
                .build();
    }

    @GetMapping("name/{name}")
    public ApiResponse<?> getSubjectByName(@PathVariable String name) {
        return ApiResponse.builder()
                .data(subjectService.getSubjectByName(name))
                .message("Subject fetched successfully")
                .build();
    }

    @GetMapping()
    public ApiResponse<?> getAllSubjects(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String... sorts) {
        return ApiResponse.builder()
                .data(subjectService.getAllSubjects(pageNo, pageSize, sorts))
                .message("Subjects fetched successfully")
                .build();
    }
    @DeleteMapping("{id}")
    public ApiResponse<?> deleteSubject(@PathVariable String id) {
        subjectService.deleteSubject(id);
        return ApiResponse.builder()
                .message("Subject deleted successfully")
                .build();
    }
}
