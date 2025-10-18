package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.VirtualTransactionResponse;
import com.grabtutor.grabtutor.entity.VirtualTransaction;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.TransactionType;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface VirtualTransactionMapper {
    default VirtualTransaction toVirtualTransaction(String courseId){
        VirtualTransaction virtualTransaction = VirtualTransaction.builder()
                .status(TransactionStatus.PENDING)
                .type(TransactionType.ENROLLMENT)
                .transactionDate(LocalDateTime.now())
                .build();
        return virtualTransaction;
    }
    default VirtualTransaction toVirtualTransaction(double withdrawAmount){
        VirtualTransaction virtualTransaction = VirtualTransaction.builder()
                .status(TransactionStatus.PENDING)
                .type(TransactionType.WITHDRAW)
                .transactionDate(LocalDateTime.now())
                .build();
        return virtualTransaction;
    }


    VirtualTransactionResponse toVirtualTransactionResponse(VirtualTransaction virtualTransaction);


}
