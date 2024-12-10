package org.grace.matjibbacked.util;

public class CustomJWTException extends RuntimeException {
    // JWT 사용자 예외 처리 클래스
    public CustomJWTException(String message) {
        super(message);
    }
}
