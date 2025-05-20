package org.example.auth.POJO.PO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 32218
 */
@Data
@AllArgsConstructor
public class RegisterPO {
    //用于注册的邮箱
    private String email;
    //用于注册的昵称
    private String username;
    //用于注册的手机号,可以任意填不做校验
    private String phone;
    //密码
    private String password;
    //性别
    private String gender;
    //头像的Url，由于路径问题没有制作
    private String avatarUrl;
    //用户的uuid
    private String uuid;
}
