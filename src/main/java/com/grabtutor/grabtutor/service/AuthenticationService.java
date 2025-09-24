package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.AuthenticationResponse;
import com.grabtutor.grabtutor.dto.response.IntrospectResponse;
import com.grabtutor.grabtutor.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    AuthenticationResponse refreshToken(RefreshRequest authenticationRequest) throws ParseException, JOSEException;
    void logout(LogoutRequest logoutRequest);
    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;
    String generateToken(User user);
    void changePassword(String userId, ChangePasswordRequest changePasswordRequest);
    void changeForgotPassword(ChangeForgotPasswordRequest request);
    String getUserIdFromSecurityContext();
}
