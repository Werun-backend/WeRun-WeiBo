package org.example.auth.Service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.Mapper.LoginMapper;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.Service.LoginService;
import org.example.auth.Utils.JwtUtils;
import org.example.auth.Utils.MD5Encryptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        loginDTO.setUserPassword(MD5Encryptor.encryptToMD5(loginDTO.getUserPassword()));
        LoginPO loginPO = loginMapper.login(loginDTO);
        if (loginPO.getId() ==0 || loginPO.getStatus()<=0) {
            throw new RuntimeException("用户名或者密码错误");
        }
        //创建JWT构造器
        JwtUtils jwtUtils = new JwtUtils();
        String jwt = jwtUtils.generateToken(loginPO.getId());
        stringRedisTemplate.opsForValue().set("jwt:"+ loginPO.getId(), jwt,3600,java.util.concurrent.TimeUnit.SECONDS);
        return new LoginVO(jwt);
    }

    @Override
    public void logout(int id) {
        stringRedisTemplate.delete("jwt:"+id);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        registerDTO.setUserPassword(MD5Encryptor.encryptToMD5(registerDTO.getUserPassword()));
        loginMapper.register(registerDTO);
    }


}
