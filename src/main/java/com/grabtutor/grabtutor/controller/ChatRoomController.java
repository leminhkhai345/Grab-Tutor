package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.impl.ChatRoomServiceImpl;
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
    ChatRoomServiceImpl chatRoomService;

    @GetMapping("/messages")
    public ApiResponse<?> loadMessages(LoadMessagesRequest request) {
        return ApiResponse.builder()
                .data(chatRoomService.loadMessages(request))
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
    @GetMapping("/{userId}")
    public ApiResponse<?> loadRooms(@PathVariable String userId){
        return ApiResponse.builder()
                .data(chatRoomService.loadMyRooms())
                .message("Load rooms successfully")
                .build();
    }
    @PutMapping("/submit/{roomId}")
    public ApiResponse<?> submitSolution(@PathVariable String roomId){
        chatRoomService.submitSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/confirm/{roomId}")
    public ApiResponse<?> confirmSolution(@PathVariable String roomId){
        chatRoomService.confirmedSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/inspect/{roomId}")
    public ApiResponse<?> inspectSolution(@PathVariable String roomId){
        chatRoomService.inspectSolution(roomId);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }
    @PutMapping("/resolve/{roomId}")
    public ApiResponse<?> submitSolution(@PathVariable String roomId, @RequestParam boolean isNormal){
        chatRoomService.resolveSolution(roomId, isNormal);
        return ApiResponse.builder()
                .message("Submit solution successfully")
                .build();
    }

}
