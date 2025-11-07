package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.response.TransactionResponse;
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


    default VirtualTransactionResponse toVirtualTransactionResponse(VirtualTransaction virtualTransaction) {
        if ( virtualTransaction == null ) {
            return null;
        }

        VirtualTransactionResponse.VirtualTransactionResponseBuilder virtualTransactionResponse = VirtualTransactionResponse.builder();
        virtualTransactionResponse.accountBalanceId( virtualTransaction.getAccountBalance().getId() );
        virtualTransactionResponse.status( virtualTransaction.getStatus() );
        virtualTransactionResponse.type( virtualTransaction.getType() );
        virtualTransactionResponse.transactionDate( virtualTransaction.getTransactionDate() );
        virtualTransactionResponse.completedAt( virtualTransaction.getCompletedAt() );
        virtualTransactionResponse.amount( virtualTransaction.getAmount() );
        if ( virtualTransaction.getPost() != null ) {
            virtualTransactionResponse.postId( virtualTransaction.getPost().getId() );
        }

        return virtualTransactionResponse.build();
    }

    default TransactionResponse toTransactionResponse(VirtualTransaction virtualTransaction){
        return TransactionResponse.builder()
                .id(virtualTransaction.getId())
                .transactionDate(virtualTransaction.getTransactionDate())
                .completedAt(virtualTransaction.getCompletedAt())
                .type(virtualTransaction.getType())
                .amount(virtualTransaction.getAmount())
                .status(virtualTransaction.getStatus())
                .userId(virtualTransaction.getAccountBalance().getUser().getId())
                .createdAt(virtualTransaction.getCreatedAt())
                .updatedAt(virtualTransaction.getUpdatedAt())
                .build();
    }
}
