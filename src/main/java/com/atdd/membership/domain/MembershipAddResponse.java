package com.atdd.membership.domain;

import com.atdd.membership.domain.enumType.MembershipType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipAddResponse {

    private final Long id;
    private final MembershipType membershipType;
}