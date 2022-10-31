package com.atdd.membership.exception;

import com.atdd.membership.domain.enumType.MembershipErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipException extends RuntimeException {

    private final MembershipErrorResult errorResult;
}
