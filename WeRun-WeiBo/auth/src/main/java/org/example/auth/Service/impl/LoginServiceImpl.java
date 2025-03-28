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
import org.example.common.model.global.AppException;
import org.example.common.model.global.HttpStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        String jwt = JwtUtils.generateToken(oneloginPO.getId());
        stringRedisTemplate.opsForValue().set("jwt:"+ oneloginPO.getId(), jwt,3600,java.util.concurrent.TimeUnit.SECONDS);
        return new LoginVO(jwt);
    }

    @Override
    public void logout(int id) {
        stringRedisTemplate.delete("jwt:"+id);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        registerDTO.setPassword(MD5Encryptor.encryptToMD5(registerDTO.getPassword()));
        loginMapper.register(new RegisterPO(registerDTO.getUserName(), registerDTO.getPhone(), registerDTO.getPassword(), registerDTO.getGender(), registerDTO.getAvatar()));
    }


}
