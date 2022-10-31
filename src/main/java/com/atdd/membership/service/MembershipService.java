package com.atdd.membership.service;

import com.atdd.membership.domain.Membership;
import com.atdd.membership.domain.MembershipAddResponse;
import com.atdd.membership.domain.MembershipDetailResponse;
import com.atdd.membership.domain.enumType.MembershipErrorResult;
import com.atdd.membership.domain.enumType.MembershipType;
import com.atdd.membership.exception.MembershipException;
import com.atdd.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
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

        return MembershipAddResponse.builder()
                .id(saveMembership.getId())
                .membershipType(saveMembership.getMembershipType())
                .build();
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        final List<Membership> memberships = membershipRepository.findAllByUserId(userId);

        return memberships.stream()
                .map(m -> MembershipDetailResponse.builder()
                        .id(m.getId())
                        .membershipType(m.getMembershipType())
                        .point(m.getPoint())
                        .createdAt(m.getCreatedAt())
                        .build())
                .collect(toList());
    }
}
