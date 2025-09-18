package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "account_verification_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Builder.Default
    RequestStatus status =  RequestStatus.PENDING;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    User user;
}
