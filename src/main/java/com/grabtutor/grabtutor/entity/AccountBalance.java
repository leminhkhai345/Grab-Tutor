package com.grabtutor.grabtutor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "accountBalance")
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "accountBalance", cascade = CascadeType.ALL)
    @JsonIgnore
    List<VirtualTransaction> virtualTransactions;
}
