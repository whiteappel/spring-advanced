package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {
/*
    if (userChangePasswordRequest.getNewPassword().length() < 8 ||
            !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
            !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
        throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
    }
    새비밀번호 필요
    최소 8자이상 문자선언
    숫자포함 필수
    대문자 포함 필수
 */
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min=8,message="새 비밀번호는 8자 이상이여야 합니다.")
    @Pattern(regexp =".*\\d.",message="새 비밀번호에는 숫자가 포함되어야합니다.")
    @Pattern(regexp =".*[A-Z].*",message="새 비밀번호에는 대문자가 포함되어야합니다.")
    private String newPassword;
}
