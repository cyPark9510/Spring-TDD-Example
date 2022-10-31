package com.atdd.membership.controller;

import com.atdd.membership.domain.MembershipRequest;
import com.atdd.membership.domain.MembershipResponse;
import com.atdd.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.atdd.membership.domain.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;


    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipResponse> addMembership(@RequestHeader(USER_ID_HEADER) final String userId,
                                                            @RequestBody @Valid final MembershipRequest membershipRequest) {

        final MembershipResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipResponse);
    }
}
