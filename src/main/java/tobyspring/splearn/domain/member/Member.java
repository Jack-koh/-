package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.*;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = PROTECTED)
@NaturalIdCache
public class Member extends AbstractEntity {
    @NaturalId
    @Embedded
    private Email email;

    @Column(name="nickname",length = 100, nullable = false)
    private String nickname;

    @Column(name="password_hash", length = 200, nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name="status", length = 20, nullable = false)
    private MemberStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다");

        this.status = MemberStatus.ACTIVE;
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        Assert.state(getStatus() == MemberStatus.ACTIVE, "등록 완료상태가 아니면 정보를 수정할 수 없습니다.");
        this.nickname = Objects.requireNonNull(updateRequest.nickname());
        this.detail.updateInfo(updateRequest);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
