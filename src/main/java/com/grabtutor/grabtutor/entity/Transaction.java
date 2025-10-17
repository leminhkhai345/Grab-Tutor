package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

    String transactionNo;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    PaymentMethod paymentMethod = PaymentMethod.VN_PAY;
    //Đây là tiền thật
    long amount;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountBalanceId", nullable = false)
    AccountBalance accountBalance;
}
