package com.atdd.membership.service;

import com.atdd.membership.domain.Membership;
import com.atdd.membership.domain.MembershipResponse;
import com.atdd.membership.domain.enumType.MembershipErrorResult;
import com.atdd.membership.domain.enumType.MembershipType;
import com.atdd.membership.exception.MembershipException;
import com.atdd.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);

        if(result != null){
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(membershipType)
                .build();

        final Membership saveMembership = membershipRepository.save(membership);

        return MembershipResponse.builder()
                .id(saveMembership.getId())
                .membershipType(saveMembership.getMembershipType())
                .build();
    }
}
