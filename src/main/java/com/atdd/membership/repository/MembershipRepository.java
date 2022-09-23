package com.atdd.membership.repository;

import com.atdd.membership.domain.Membership;
import com.atdd.membership.domain.enumType.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(String UserId, MembershipType membershipType);
}
