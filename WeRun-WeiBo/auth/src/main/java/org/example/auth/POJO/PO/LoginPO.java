package org.example.auth.POJO.PO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginPO {
    //查找用户ID，若账号密码没有匹配成功显示登陆失败
    @NotNull(message = "账号或密码错误")
    private int id;
    //查找状态码，若用户已经被假删除则已然返回登录失败信息
    @Min(value = 1, message = "账号或密码错误")
    private int status;
}
