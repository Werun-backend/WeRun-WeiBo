package org.example.auth.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.auth.POJO.BO.CheckEmailBO;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.DTO.ResetDTO;
import org.example.auth.POJO.DTO.ResetPasswordDTO;
import org.example.auth.POJO.VO.LoginInfVO;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.service.LoginService;
import org.example.common.model.global.BaseResult;
import org.example.common.model.util.JwtUtils;
import org.example.common.model.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @PostMapping("/login/common")
    public BaseResult<Object> login(@RequestBody @Valid LoginDTO loginDTO) {
        return BaseResult.success("登录成功",loginService.login(loginDTO).join());
    }
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody LoginVO request) {
        String refreshToken = request.getRefreshToken();

        // 验证Refresh Token有效性
        try {
            JwtUtils.parseJWT(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
        // 生成新Token
        Claims claims = JwtUtils.parseJWT(refreshToken);
        String newAccessToken = JwtUtils.generateJwt(claims);

        // 更新Redis（存储用户最新Token）
        ThreadContext.setThreadLocal(newAccessToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .body("Token refreshed");
    }
    @GetMapping("/login/emailSend")
    public BaseResult<Object> mailLogin(@NotNull @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "请填入正确的邮箱的格式")String Email) {
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
        LoginInfVO loginVO;
        try {
            loginVO = loginService.LoginByCode(l);
        } catch (Exception e) {
            logger.error("发生了错误:{}",e.getMessage());
            return BaseResult.error("验证码错误",null);
        }
        return BaseResult.success("登录成功", loginVO);
    }
    @PostMapping("/logout")
    public BaseResult<Object> logout(@RequestHeader(value = "Authorization") String token) {
        loginService.logout(token);
        return BaseResult.success("退出成功", token);
    }

    @PostMapping("/register/sendCode")
    public BaseResult<Object> registerSend(@RequestBody @Valid RegisterDTO registerDTO, MultipartFile file) throws IOException {
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
}
