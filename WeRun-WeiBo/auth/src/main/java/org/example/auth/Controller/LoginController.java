package org.example.auth.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.auth.POJO.BO.CheckEmailBO;
import org.example.auth.POJO.DTO.*;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.Service.LoginService;
import org.example.common.model.global.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @PostMapping("/login/common")
    public BaseResult<Object> login(@RequestBody @Valid LoginDTO loginDTO) {
        //校验登录
        LoginVO loginVO;
        try {
            loginVO = loginService.login(loginDTO);
        } catch (Exception e) {
            logger.error("发生错误:{}",e.getMessage());
            return BaseResult.error("密码错误");
        }
        return BaseResult.success("登录成功", loginVO.getToken());
    }
    @GetMapping("/login/emailSend")
    public BaseResult<Object> mailLogin(@NotNull @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "请填入正确的邮箱的格式")String Email) {
        //校验登录//    @NotNull(message = "邮箱不能为空")
        //
        logger.debug("传入参数:{}",Email);
        try {
            loginService.mLogin(Email);
        } catch (Exception e) {
            logger.error("发生错误:{}",e.getMessage());
            return BaseResult.error("邮箱验证码发送错误");
        }
        return BaseResult.success("发送成功", null);
    }
    @PostMapping("/login/emailCode")
    public BaseResult<Object> LoginByCode(@RequestBody @Valid CheckEmailBO l) {
        //校验登录
        LoginVO loginVO;
        try {
            loginVO = loginService.LoginByCode(l);
        } catch (Exception e) {
            logger.error("发生了错误:{}",e.getMessage());
            return BaseResult.error("验证码错误",null);
        }
        return BaseResult.success("登录成功", loginVO.getToken());
    }
    @PostMapping("/logout")
    public BaseResult<Object> logout(@RequestHeader(value = "Authorization") String token) {
        loginService.logout(token);
        return BaseResult.success("退出成功", token);
    }

    @PostMapping("/register/sendCode")
    public BaseResult<Object> registerSend(@RequestBody @Valid RegisterDTO registerDTO,MultipartFile file) throws IOException {
        loginService.register(registerDTO,file);
        return BaseResult.success("发送成功",null);
    }
    @PostMapping("/register/OK")
    public BaseResult<Object> register(@RequestBody @Valid RegisterDTO registerDTO,MultipartFile file,String code) throws IOException {
        LoginDTO loginDTO = new LoginDTO(registerDTO.getPhone(),registerDTO.getPassword());
        loginService.registerOK(registerDTO,file,code);
        BaseResult<Object> result = login(loginDTO);
        return BaseResult.success("完成注册并登录",result.getData());
    }
    @PostMapping("/Reset")
    public BaseResult<Object> Reset(@RequestBody @Valid ResetDTO resetDTO){
        loginService.Reset(resetDTO);
        return BaseResult.success("已发送重置密码",null);
    }
    @PostMapping("/ResetPassword")
    public BaseResult<Object> ResetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        loginService.ResetPassword(resetPasswordDTO);
        return BaseResult.success("完成重置密码并登录",null);
    }

    @GetMapping("/login/github")
    public BaseResult<Object> loginByGithub(Model model, @RegisteredOAuth2AuthorizedClient(registrationId = "github") OAuth2AuthorizedClient authorizedClient,
                            @AuthenticationPrincipal OAuth2User oauth2User) {
            model.addAttribute("userName", oauth2User.getName());
            model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
            model.addAttribute("userAttributes", oauth2User.getAttributes());
            return BaseResult.success("github登录成功",null);
        }

}
