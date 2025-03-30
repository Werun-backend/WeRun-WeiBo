package org.example.auth.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.auth.POJO.DTO.LoginByCodeDTO;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.PO.RegisterPO;

import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Phone = #{Phone} and Password =#{Password}")
    List<LoginPO> login(LoginDTO loginDTO);
    @Insert("insert into WeiBo.Users (Username,Phone,Password,Gender,AvatarURL,uuid) values (#{Username},#{Phone},#{Password},#{Gender},#{AvatarURL},#{uuid})")
    void register(RegisterPO registerPO);
    @Select("select count(*) from WeiBo.Users where Email = #{Email}")
    int checkMail(String email);
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Email = #{Email}")
    List<LoginPO> LoginByCode(String EMAIL);
}
