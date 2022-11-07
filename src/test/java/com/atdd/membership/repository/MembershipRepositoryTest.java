package com.atdd.membership.repository;

import com.atdd.membership.domain.Membership;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.atdd.membership.domain.enumType.MembershipType.KAKAO;
import static com.atdd.membership.domain.enumType.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

/*
 JPA Repository들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 한다.
 @DataJpaTest 내부를 확인해보면 @ExtendWith( SpringExtension.class) 어노테이션을 가지고 있어 따로 테스트 컨텍스트를 잡아주지 않아도 된다.
                            + @Transactional 어노테이션이 있어서 테스트의 롤백 등을 위한 트랜잭션 어노테이션을 추가하지 않아도 된다.
 */
@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

//    /**
//     * 에러 해결
//     * 1. MembershipRepository 인터페이스 생성
//     * 2. Membership 클래스 생성
//     * 3. Membership 클래스가 JPA에 의해 관리되도록 @Entity 추가( + 기본 생성자 추가)
//     * 4. Membership 클래스에 식별자(@Id) 추가
//     *
//     * 테스트 성공 후 해당 테스트는 불필요한 테스트이므로 제거
//     * (리팩토링의 단계 진행)
//     */
//    @Test
//    public void MembershipRepositoryIsNotNull() {
//        assertThat(membershipRepository).isNotNull();
//    }

    /**
     * 멤버십 등록 Repository 테스트
     *
     * 에러 해결
     * 1. Membership 클래스 생성자, 접근자 및 필드 추가
     * 2. MembershipType 열거형 생성
     * 3. Membership 클래스 membershipType 필드 타입 수정
     */
    @Test
    public void 멤버십등록테스트() {
        // given
        final Membership membership = Membership.builder()
                .userId("사용자 아이디")
                .membershipType(NAVER)
                .point(10000)
                .build();

        // when
        final Membership result = membershipRepository.save(membership);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("사용자 아이디");
        assertThat(result.getMembershipType()).isEqualTo(NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    /**
     * 멤버십 존재확인 Repository 테스트
     *
     * 에러 해결
     * 1. MembershipRepository 인터페이스에 findByUserIdAndMembershipType 메소드 추가
     */
    @Test
    public void 멤버십이존재하는지테스트() {
        //given
        final Membership membership = Membership.builder()
                .userId("사용자 아이디")
                .membershipType(NAVER)
                .point(10000)
                .build();

        // when
        membershipRepository.save(membership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("사용자 아이디", NAVER);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("사용자 아이디");
        assertThat(findResult.getMembershipType()).isEqualTo(NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);
    }

    @Test
    public void 멤버십조회_사이즈가0() {
        // given

        // when
        final List<Membership> result =  membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void 멤버십조회_사이즈가2() throws Exception {
        // given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        // when
        final List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 멤버식추가후삭제() {
        // given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(NAVER)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);

        // when
        membershipRepository.deleteById(naverMembership.getId());

        // then
        Optional<Membership> deleteMembership = membershipRepository.findById(naverMembership.getId());
        assertThat(deleteMembership.isPresent()).isFalse();
    }
}
