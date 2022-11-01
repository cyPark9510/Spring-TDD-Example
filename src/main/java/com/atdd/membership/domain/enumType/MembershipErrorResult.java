package com.atdd.membership.domain.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {

    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    UNKNOWN_EXCEPION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
