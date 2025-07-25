package tobyspring.splearn.domain.shared;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {
    @Test
    void equality() {
        var email1 = new Email("rhgusdud09@naver.com");
        var email2 = new Email("rhgusdud09@naver.com");

        assertThat(email1).isEqualTo(email2);
    }
}