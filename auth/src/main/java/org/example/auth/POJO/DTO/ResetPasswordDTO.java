package org.example.auth.POJO.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 32218
 */
@Data
@AllArgsConstructor
public class ResetPasswordDTO {
    //这是手机号，必须填写且符合格式
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "请填入正确的手机号的格式")
    private String phone;
    //六位数验证码
    @NotNull(message = "验证码不能为空")
    @Pattern(regexp = "^[0-9]{6}$",message = "请填入6位数字验证码")
    private String code;
    //密码
    @NotNull(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\da-zA-Z\\s]).{1,20}$",message = "至少包含字母、数字、特殊字符，1-20位")
    private String password;
    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "请填入正确的邮箱的格式")
    private String email;
}
