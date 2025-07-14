package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {
    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "rhgusdud09", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("rhgusdud09");
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "rhgusdud09", "자기소개"));

        Member member2 = registerMember("rhgusdud00@naver.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2 는 기존의 member 와 같은 profile 을 사용할 수 없다
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "rhgusdud09", "Introduction"));
        }).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "rhgusdud01", "Introduction"));

        // 기존 프로필 주소로는 바꾸는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "rhgusdud09", "Introduction"));

        // 프로필 주소를 제거하는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "", "Introduction"));

        // 프로필 주소 중복은 허용하지 않음
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "rhgusdud01", "Introduction"));
        }).isInstanceOf(DuplicateProfileException.class);

    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    void memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("rhgusdud09@naver.com", "Jack", "longsecret"));
        checkValidation(new MemberRegisterRequest("rhgusdud09@naver.com", "Jacky___________________________", "longsecret"));
        checkValidation(new MemberRegisterRequest("rhgusdud09naver.com", "Jacky", "longsecret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        Assertions.assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
