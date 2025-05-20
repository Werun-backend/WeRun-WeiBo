package org.example.auth.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.PO.RegisterPO;
import org.example.auth.POJO.PO.ResetPO;
import org.example.common.model.user.UserBO;

import java.util.List;

/**
 * @author 黄湘湘
 */
@Mapper
public interface LoginMapper {
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Phone = #{Phone} and Password =#{Password}")
    List<UserBO> login(LoginDTO loginDTO);
    @Insert("insert into WeiBo.Users (Email,Username,Phone,Password,Gender,AvatarURL,uuid) values (#{Email},#{Username},#{Phone},#{Password},#{Gender},#{AvatarURL},#{uuid})")
    void register(RegisterPO registerPO);
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Email = #{Email}")
    List<UserBO> LoginByCode(String Email);
    @Update("update WeiBo.Users set Password = #{Password} where Email = #{Email} and Phone =#{Phone}")
    void ResetPassword(ResetPO resetPO);
    @Select("select count(*) from WeiBo.Users where Phone = #{Phone} or Email = #{Email}")
    int checkUnique(String Phone, String Email);
}
