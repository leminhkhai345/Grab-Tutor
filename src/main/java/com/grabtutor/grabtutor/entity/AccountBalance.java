package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "account_balance")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance extends BaseEntity{
    double balance = 0;

    @OneToMany(mappedBy = "accountBalance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Transaction> transactions;

    @OneToOne(mappedBy = "accountBalance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;
}
