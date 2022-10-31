package com.atdd.membership.repository;

import com.atdd.membership.domain.Membership;
import com.atdd.membership.domain.enumType.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(final String UserId, final MembershipType membershipType);

    List<Membership> findAllByUserId(final String userId);
}
