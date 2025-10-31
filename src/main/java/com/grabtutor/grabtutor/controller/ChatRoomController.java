package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomController {
    ChatRoomService chatRoomService;

    @GetMapping("/message")
    public ApiResponse<?> loadMessages(@RequestParam String roomId) {
        return ApiResponse.builder()
                .data(chatRoomService.loadMessages(roomId))
                .message("Load message successfully")
                .build();
    }
    @GetMapping("/myRooms")
    public ApiResponse<?> loadMyRooms(){
        return ApiResponse.builder()
                .data(chatRoomService.loadMyRooms())
                .message("Load rooms successfully")
                .build();
    }
    @GetMapping
    public ApiResponse<?> loadRooms(@RequestParam String roomId){
        return ApiResponse.builder()
                .data(chatRoomService.loadRooms(roomId))
                .message("Load rooms successfully")
                .build();
    }
    @PutMapping("/submit")
    public ApiResponse<?> submitSolution(@RequestParam String roomId){
        chatRoomService.submitSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/confirm")
    public ApiResponse<?> confirmSolution(@RequestParam String roomId){
        chatRoomService.confirmedSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/inspect")
    public ApiResponse<?> inspectSolution(@RequestParam String roomId){
        chatRoomService.inspectSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/resolve")
    public ApiResponse<?> submitSolution(@RequestParam String roomId, @RequestParam boolean isNormal){
        chatRoomService.resolveSolution(roomId, isNormal);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }

}
