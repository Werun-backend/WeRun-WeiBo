package org.example.auth.Service;

import jakarta.validation.Valid;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.VO.LoginVO;

public interface LoginService {
    LoginVO login(LoginDTO loginDTO);

    void logout(int id);


    void register(RegisterDTO registerDTO);
}
