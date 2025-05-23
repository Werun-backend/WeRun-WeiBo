package org.example.auth.service;

import jakarta.validation.Valid;
import org.example.auth.POJO.BO.CheckEmailBO;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.DTO.ResetDTO;
import org.example.auth.POJO.DTO.ResetPasswordDTO;
import org.example.auth.POJO.VO.LoginInfVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author 32218
 */
public interface LoginService {
    CompletableFuture<LoginInfVO> login(@Valid LoginDTO loginDTO);

    void logout(String token);

    void register(@Valid RegisterDTO registerDTO, MultipartFile file) throws IOException;

    void mLogin(String Email);

    LoginInfVO LoginByCode(@Valid CheckEmailBO l);

    void Reset(@Valid ResetDTO resetDTO);

    void ResetPassword(@Valid ResetPasswordDTO resetPasswordDTO);

    void registerOK(@Valid RegisterDTO registerDTO, MultipartFile file, String code) throws IOException;
}
