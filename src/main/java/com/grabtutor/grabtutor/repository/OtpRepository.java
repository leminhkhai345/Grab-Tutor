package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,String> {

    Object getOtpByCode(String code);

    List<Otp> findOtpByCode(String code);
}
