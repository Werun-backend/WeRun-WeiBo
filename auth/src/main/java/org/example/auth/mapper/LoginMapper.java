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
 * @author 32218
 */
@Mapper
public interface LoginMapper {
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Phone = #{phone} and Password =#{Password}")
    List<UserBO> login(LoginDTO loginDto);
    @Insert("insert into WeiBo.Users (Email,Username,Phone,Password,Gender,AvatarURL,uuid) values (#{email},#{Username},#{phone},#{Password},#{Gender},#{AvatarURL},#{uuid})")
    void register(RegisterPO registerPo);
    @Select("select UUID, USERNAME, PHONE, EMAIL, PASSWORD, STATUS, SIGNATURE, AVATARURL, ADDRESS, CREATED_AT, UPDATED_AT, GENDER, BIRTHDAY from WeiBo.Users where Email = #{email}")
    List<UserBO> loginByCode(String email);
    @Update("update WeiBo.Users set Password = #{Password} where Email = #{email} and Phone =#{phone}")
    void resetPassword(ResetPO resetPo);
    @Select("select count(*) from WeiBo.Users where Phone = #{phone} or Email = #{email}")
    int checkUnique(String phone, String email);
}
