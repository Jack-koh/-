//package tobyspring.splearn.application.provided;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.test.util.ReflectionTestUtils;
//import tobyspring.splearn.application.MemberService;
//import tobyspring.splearn.application.required.EmailSender;
//import tobyspring.splearn.application.required.MemberRepository;
//import tobyspring.splearn.domain.Email;
//import tobyspring.splearn.domain.Member;
//import tobyspring.splearn.domain.MemberStatus;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static tobyspring.splearn.domain.MemberFixture.*;
//import static tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;
//
//class MemberRegisterManualTest {
//    @Test
//    void registerTestStub() {
//        MemberRegister register = new MemberService(new MemberRepositoryStub(), new EmailSenderStub(), createPasswordEncoder());
//
//        Member member = register.register(createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//    }
//
//    @Test
//    void registerTestMock() {
//        EmailSenderMock emailSenderMock = new EmailSenderMock();
//        MemberRegister register = new MemberService(new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder());
//
//        Member member = register.register(createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
//        assertThat(emailSenderMock.getTos()).hasSize(1);
//        assertThat(emailSenderMock.getTos().get(0)).isEqualTo(member.getEmail());
//    }
//
//    @Test
//    void registerTestMockito() {
//        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
//        MemberRegister register = new MemberService(new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder());
//
//        Member member = register.register(createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
//        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
//    }
//
//    static class MemberRepositoryStub implements MemberRepository {
//        @Override
//        public Member save(Member member) {
//            ReflectionTestUtils.setField(member, "id", 1L);
//            return member;
//        }
//
//        @Override
//        public Optional<Member> findByEmail(Email email) {
//            return Optional.empty();
//        }
//    }
//
//    static class EmailSenderStub implements EmailSender {
//        @Override
//        public void send(Email email, String subject, String body) {
//
//        }
//    }
//
//    static class EmailSenderMock implements EmailSender {
//        List<Email> tos = new ArrayList<>();
//
//        @Override
//        public void send(Email email, String subject, String body) {
//            tos.add(email);
//        }
//
//        public List<Email> getTos() {
//            return tos;
//        }
//    }
//}