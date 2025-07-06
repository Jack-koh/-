package tobyspring.splearn.adapter.integration;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.Email;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DummyEmailSenderTest {
    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("rhgusdud09@naver.com"), "subject", "body");

        assertThat(out.capturedLines()[0])
                .isEqualTo("DummyEmailSender send email: Email[address=rhgusdud09@naver.com]");
    }
}