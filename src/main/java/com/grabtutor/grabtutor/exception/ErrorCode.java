package com.grabtutor.grabtutor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_BLOCKED(1007, "User blocked", HttpStatus.FORBIDDEN),
    USER_IS_NOT_TUTOR(1010, "User is not a tutor", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS(1002, "Username already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(1003, "Email already exists", HttpStatus.CONFLICT),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NATIONAL_ID_ALREADY_EXISTS(1005, "National id already exists", HttpStatus.CONFLICT),
    ACCOUNT_ALREADY_VERIFIED(1006, "Account already verified", HttpStatus.CONFLICT),
    POST_NOT_EXIST(2001, "Post does not exist", HttpStatus.NOT_FOUND),
    TUTOR_INFO_NOT_FOUND(2002, "Tutor info not found", HttpStatus.NOT_FOUND),
    REVIEW_ALREADY_EXISTS(2003, "Review already exists", HttpStatus.CONFLICT),

    SUBJECT_ALREADY_EXISTS(3001, "Subject already exists", HttpStatus.CONFLICT),
    SUBJECT_NOT_FOUND(3002, "Subject not found", HttpStatus.NOT_FOUND),

    INVALID_KEY(4001, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1007, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD(1009, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(4002, "Token is invalid", HttpStatus.UNAUTHORIZED),

    OTP_INVALID(5001, "OTP invalid", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(5002, "OTP expired", HttpStatus.BAD_REQUEST),
    OTP_USED(5003, "OTP used", HttpStatus.BAD_REQUEST),
    SESSION_END(5004, "Session ended", HttpStatus.BAD_REQUEST),

    POST_NOT_SOLVED(6000, "Post is not solved yet", HttpStatus.BAD_REQUEST),
    REPORT_NOT_EXIST(6001, "Report does not exist", HttpStatus.NOT_FOUND),
    POST_ALREADY_ACCEPTED(6003, "Post already accepted", HttpStatus.CONFLICT),
    REPORT_ALREADY_RESOLVED(6002, "Report already resolved", HttpStatus.CONFLICT),
    POST_NOT_FOUND(6004, "Post not found", HttpStatus.NOT_FOUND),
    CANNOT_DELETE_POST_WITH_ACCEPTED_BID(6005, "Cannot delete post with accepted bid", HttpStatus.BAD_REQUEST),
    CANNOT_CHANGE_ADMIN_ACTIVE_STATUS(6006, "Cannot change active status of admin account", HttpStatus.BAD_REQUEST),

    REVIEW_NOT_EXIST(7001, "Review does not exist", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(8001, "Course not found", HttpStatus.NOT_FOUND),
    LESSON_NOT_FOUND(9001, "Lesson not found", HttpStatus.NOT_FOUND),

    TUTOR_NOT_AUTHORIZED(9002, "Tutor not authorized", HttpStatus.FORBIDDEN),

    CHAT_ROOM_NOT_FOUND(10001, "Chat room not found", HttpStatus.NOT_FOUND),
    CHAT_ROOM_NOT_IN_DISPUTED(10002,"Chatroom status not in DISPUTED ", HttpStatus.CONFLICT),
    MESSAGE_NOT_FOUND(10003, "Message not found", HttpStatus.NOT_FOUND),

    ACCOUNT_BALANCE_NOT_FOUND(11001, "Account balance not found", HttpStatus.NOT_FOUND),
    ACCOUNT_DONT_HAVE_ENOUGH_MONEY(11002, "Account doesn't have enough money", HttpStatus.BAD_REQUEST),
    USER_ALREADY_ENROLLED_COURSE(11003, "User already enrolled in this course", HttpStatus.CONFLICT),
    CANNOT_DELETE_OWN_ACCOUNT(11004, "Cannot delete own account", HttpStatus.BAD_REQUEST),

    VERIFICATION_REQUEST_NOT_FOUND(12001, "Verification request not found", HttpStatus.NOT_FOUND),
    INVALID_EMAIL_OR_PASSWORD(12002, "Invalid email or password", HttpStatus.UNAUTHORIZED),
    LESSON_NOT_ACCESSIBLE(12003, "Lesson not accessible", HttpStatus.FORBIDDEN),
    ALREADY_HAVE_PENDING_REQUEST(12004, "Already have pending request", HttpStatus.CONFLICT),

    TUTOR_BID_NOT_FOUND(13001, "Tutor bid not found", HttpStatus.NOT_FOUND),
    BID_NOT_PENDING(13002, "Cant accept non pending tutor bid", HttpStatus.BAD_REQUEST),
    ALREADY_PROPOSE_BID(13003, "Already propose bid to this post", HttpStatus.CONFLICT),
    UNAUTHORIZED_ACCESS(13004, "You are not authorized to access this resource", HttpStatus.FORBIDDEN),
    TRANSACTION_FAILED(14001, "Transaction failed", HttpStatus.BAD_REQUEST),

    FORBIDDEN(88888, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    UNCATEGORIZED(99999, "Uncategorized", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER(77777, "Missing parameter: {parameterName}", HttpStatus.BAD_REQUEST)
    ;
    private final long code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(long code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
