package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void profile() {
        new Profile("jack");
        new Profile("asd");
        new Profile("bbas");
        new Profile("1234s");
        new Profile("");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(()-> new Profile("toolongtoolongtoolongtoolong")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        var profile = new Profile("jack");
        assertThat(profile.url()).isEqualTo("@jack");
    }
}