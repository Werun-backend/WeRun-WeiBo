package org.example.auth.Mapper;

import ch.qos.logback.classic.spi.EventArgUtil;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.PO.LoginPO;

@Mapper
public interface LoginMapper {

    LoginPO login(@Valid LoginDTO loginDTO);
}
