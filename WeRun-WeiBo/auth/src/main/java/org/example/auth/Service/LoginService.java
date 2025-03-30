package org.example.auth.Service;

import jakarta.validation.Valid;
import org.example.auth.POJO.BO.CheckEmailBO;
import org.example.auth.POJO.DTO.*;
import org.example.auth.POJO.VO.LoginVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LoginService {
    LoginVO login(@Valid LoginDTO loginDTO);

    void logout(String jwt);

    void register(@Valid RegisterDTO registerDTO, MultipartFile file) throws IOException;

    void mLogin(@Valid MLoginDTO mloginDTO);

    LoginVO LoginByCode(@Valid CheckEmailBO l);

    void Reset(@Valid ResetDTO resetDTO);

    void ResetPassword(@Valid ResetPasswordDTO resetPasswordDTO);

    void registerOK(@Valid RegisterDTO registerDTO, MultipartFile file, String code) throws IOException;
}
