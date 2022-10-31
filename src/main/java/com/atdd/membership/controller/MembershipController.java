package com.atdd.membership.controller;

import com.atdd.membership.domain.MembershipAddResponse;
import com.atdd.membership.domain.MembershipDetailResponse;
import com.atdd.membership.domain.MembershipRequest;
import com.atdd.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.atdd.membership.domain.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;


    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(@RequestHeader(USER_ID_HEADER) final String userId,
                                                               @RequestBody @Valid final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> addMembership(@RequestHeader(USER_ID_HEADER) final String userId) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }
}
