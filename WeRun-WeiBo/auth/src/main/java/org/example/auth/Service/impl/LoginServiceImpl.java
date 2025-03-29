package org.example.auth.Service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.example.auth.Mapper.LoginMapper;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.PO.RegisterPO;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.Service.LoginService;
import org.example.auth.Utils.JwtUtils;
import org.example.auth.Utils.MD5Encryptor;
import org.example.auth.Utils.RedisIdWorker;
import org.example.common.model.global.AppException;
import org.example.common.model.global.HttpStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        loginDTO.setPassword(MD5Encryptor.encryptToMD5(loginDTO.getPassword()));
        List<LoginPO> loginPO = loginMapper.login(loginDTO);
        List<LoginPO> newLoginPO = loginPO.stream().filter(aloginPO -> aloginPO.getStatus() > 0).toList();
        if (newLoginPO.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,"用户名或者密码错误",null);
        }
        //利用负载均衡从登录列表中随机获取一个
        LoginPO oneloginPO = newLoginPO.get(RandomUtil.randomInt(0, newLoginPO.size()-1));
        //创建JWT构造器
        Map<String,Object> map =new HashMap<>();
        map.put("users", oneloginPO);
        String jwt = JwtUtils.generateToken(map);
        return new LoginVO(jwt);
    }

    @Override
    public void logout(String jwt) {
        stringRedisTemplate.opsForValue().set("blacklist:jwt", jwt);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        registerDTO.setPassword(MD5Encryptor.encryptToMD5(registerDTO.getPassword()));
        RedisIdWorker redisIdWorker = new RedisIdWorker(stringRedisTemplate);
        loginMapper.register(new RegisterPO(
                registerDTO.getUserName(),
                registerDTO.getPhone(),
                registerDTO.getPassword(),
                registerDTO.getGender(),
                registerDTO.getAvatar(),
                String.valueOf(redisIdWorker.nextId("uuid"))
                )
        );
    }


}
