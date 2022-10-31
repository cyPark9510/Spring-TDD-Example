package com.atdd.membership.controller;

import com.atdd.membership.config.GlobalExceptionHandler;
import com.atdd.membership.domain.MembershipDetailResponse;
import com.atdd.membership.domain.MembershipRequest;
import com.atdd.membership.domain.MembershipAddResponse;
import com.atdd.membership.domain.enumType.MembershipErrorResult;
import com.atdd.membership.domain.enumType.MembershipType;
import com.atdd.membership.exception.MembershipException;
import com.atdd.membership.service.MembershipService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.atdd.membership.domain.MembershipConstants.USER_ID_HEADER;
import static com.atdd.membership.domain.enumType.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;
    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        gson = new Gson();
    }

    @Test
    public void mockMvc가Null이아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

//    @Test
//    public void 멤버십등록실패_포인트가Null() throws Exception {
//        // given
//        final String url = "/api/v1/memberships";
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER, "userId")
//                        .content(gson.toJson(membershipRequest(null, NAVER)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 멤버십등록실패_포인트가음수() throws Exception {
//        // given
//        final String url = "/api/v1/memberships";
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER, "userId")
//                        .content(gson.toJson(membershipRequest(-1, NAVER)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 멤버십등록실패_멤버십종류가Null() throws Exception {
//        // given
//        final String url = "/api/v1/memberships";
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER, "userId")
//                        .content(gson.toJson(membershipRequest(10000, null)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
    /**
     * 위 3가지 테스트는 코드가 유사하고 파라미터만 다른 중복된 테스트이다.
     *
     * Junit5에서는 동일한 테스트 케이스에 대해 파라미터를 다르게 실행할 수 있는 기능을 @ParameterizedTest를 통해 제공한다.
     * 다음 테스트 코드는 @ParameterizedTest를 통해 3가지 케이스를 1개의 테스트로 만들고 파라미터만 다르게 하여 중복은 제거한다.
     */
    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")  // 파라미터 작성 함수 이름
    public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, NAVER),
                Arguments.of(-1, NAVER),
                Arguments.of(10000, null)
        );
    }


    @Test
    public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("userId", NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(10000, NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipResponse = MembershipAddResponse.builder()
                .id(-1L)
                .membershipType(NAVER)
                .build();

        doReturn(membershipResponse).when(membershipService).addMembership("userId", NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(10000, NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(NAVER);
        assertThat(response.getId()).isNotNull();
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }

    @Test
    public void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    
    @Test
    public void 멤버십목록조회성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build()
        )).when(membershipService).getMembershipList("userId");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "userId")
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}
