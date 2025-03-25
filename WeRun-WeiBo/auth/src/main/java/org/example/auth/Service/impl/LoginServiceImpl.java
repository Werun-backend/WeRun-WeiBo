package org.example.auth.Service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.Mapper.LoginMapper;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.Service.LoginService;
import org.example.auth.Utils.JwtUtils;
import org.example.auth.Utils.RedisIdWorker;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisIdWorker redisIdWorker;
    @Override
    public void login(LoginDTO loginDTO) {
        LoginPO loginPO = loginMapper.login(loginDTO);
        if (loginPO.getId() ==0 || loginPO.getStatus()<=0) {
            throw new RuntimeException("用户名或者密码错误");
        }
        //创建JWT构造器
        JwtUtils jwtUtils = new JwtUtils();
        stringRedisTemplate.opsForValue().set("jwt:"+redisIdWorker.nextId("token"), jwtUtils.generateToken(loginPO.getId()));
    }
}
