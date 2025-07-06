package tobyspring.splearn.domain;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("rhgusdud09@naver.com");
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "Jacky", "long-secret");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
