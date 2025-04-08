package org.example.auth.Mapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.PO.RegisterPO;
import org.example.auth.POJO.PO.ResetPO;

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
    @Update("update WeiBo.Users set Password = #{Password} where Email = #{Email} and Phone =#{Phone}")
    void ResetPassword(ResetPO resetPO);
    @Select("select count(*) from WeiBo.Users where Phone = #{Phone} or Email = #{Email}")
    int checkUnique(String phone, String email);
}
