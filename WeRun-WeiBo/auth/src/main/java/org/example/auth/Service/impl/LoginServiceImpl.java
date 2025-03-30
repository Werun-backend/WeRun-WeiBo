package org.example.auth.Service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.example.auth.Mapper.LoginMapper;
import org.example.auth.POJO.DTO.*;
import org.example.auth.POJO.PO.LoginPO;
import org.example.auth.POJO.PO.RegisterPO;
import org.example.auth.POJO.PO.ResetPO;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.Service.LoginService;
import org.example.auth.Utils.JwtUtils;
import org.example.auth.Utils.MD5Encryptor;
import org.example.auth.Utils.RedisIdWorker;
import org.example.common.model.global.AppException;
import org.example.common.model.global.HttpStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final MailSender sender;
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        loginDTO.setPassword(MD5Encryptor.encryptToMD5(loginDTO.getPassword()));
        List<LoginPO> loginPO = loginMapper.login(loginDTO);
        return jwtMaker(loginPO);
    }

    private static LoginVO jwtMaker(List<LoginPO> loginPO) {
        List<LoginPO> newLoginPO = loginPO.stream().filter(aloginPO -> aloginPO.getStatus() > 0).toList();
        if (newLoginPO.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,"登录错误",null);
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
    public void register(RegisterDTO registerDTO, MultipartFile file) throws IOException {
        RedisIdWorker redisIdWorker = new RedisIdWorker(stringRedisTemplate);
        String imagePath = "/var/weiboImage" + redisIdWorker
                .nextId("image") +
                "." +
                Objects.requireNonNull(file.getOriginalFilename()).
                        substring(file.
                                getOriginalFilename().
                                lastIndexOf(".") + 1);
        file.transferTo(new File(imagePath));
        registerDTO.setPassword(MD5Encryptor.encryptToMD5(registerDTO.getPassword()));
        loginMapper.register(new RegisterPO(
                registerDTO.getUserName(),
                registerDTO.getPhone(),
                registerDTO.getPassword(),
                registerDTO.getGender(),
                imagePath,
                String.valueOf(redisIdWorker.nextId("uuid"))
                )
        );
    }

    @Override
    public void mLogin(MLoginDTO mloginDTO) {
        sendCode(mloginDTO.getEmail(), "code");
    }

    private void sendCode(String email, String type) {
        if (stringRedisTemplate.hasKey("email:code:"+email)) {
            throw new AppException(HttpStatus.BAD_REQUEST,"验证码已发送",null);
        }
        if(loginMapper.checkMail(email)<0){
            throw new AppException(HttpStatus.BAD_REQUEST,"邮箱未绑定手机号",null);
        }
        SimpleMailMessage mail =new SimpleMailMessage();
        mail.setSubject("验证码");
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set("email:"+ type +":"+email, code, 600, java.util.concurrent.TimeUnit.SECONDS);
        mail.setText("验证码为："+ code);
        mail.setTo(email);
        mail.setFrom("3221834658@qq.com");
        sender.send(mail);
    }

    @Override
    public LoginVO LoginByCode(LoginByCodeDTO l) {
        CheckCode(l,"code");
        List<LoginPO> loginPO = loginMapper.LoginByCode(l.getEmail());
        return jwtMaker(loginPO);
    }

    private void CheckCode(LoginByCodeDTO l,String key) {
        if (stringRedisTemplate.hasKey("email:"+key+":"+ l.getEmail())) {
            String code = stringRedisTemplate.opsForValue().get("email:"+key+":"+ l.getEmail());
            if (code != null && !code.equals(l.getCode())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "验证码错误", null);
            }
        }else {
            throw new AppException(HttpStatus.BAD_REQUEST,"验证码已过期",null);
        }
    }

    @Override
    public void Reset(ResetDTO resetDTO) {
        sendCode(resetDTO.getEmail(), "reset");
    }

    @Override
    public void ResetPassword(ResetPasswordDTO resetPasswordDTO) {
        CheckCode(new LoginByCodeDTO(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode()),"reset");
        resetPasswordDTO.setPassword(MD5Encryptor.encryptToMD5(resetPasswordDTO.getPassword()));
        ResetPO resetPO = new ResetPO(resetPasswordDTO.getEmail(), resetPasswordDTO.getPhone(),resetPasswordDTO.getPassword());
        loginMapper.ResetPassword(resetPO);
    }


}
