package org.example.auth.Mapper;

import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.PO.RegisterPO;

import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("select Id,Status from WeiBo.Users where Phone = #{Phone} and Password =#{Password}")
    List<LoginPO> login(LoginDTO loginDTO);
    @Insert("insert into WeiBo.Users (Username,Phone,Password,Gender,AvatarURL) values (#{Username},#{Phone},#{Password},#{Gender},#{AvatarURL})")
    void register(RegisterPO registerPO);
}
