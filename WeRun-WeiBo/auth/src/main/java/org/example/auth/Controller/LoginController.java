package org.example.auth.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.Service.LoginService;
import org.example.common.model.global.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @PostMapping("/login")
    public BaseResult login(@RequestBody @Valid LoginDTO loginDTO) {
        //校验登录
        try {
            loginService.login(loginDTO);
        } catch (Exception e) {
            return BaseResult.error("用户名或者密码错误");
        }
            return BaseResult.success("登录成功");
    }
}
