package org.example.auth.Mapper;

import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.PO.LoginPO;

@Mapper
public interface LoginMapper {

    LoginPO login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);
}
