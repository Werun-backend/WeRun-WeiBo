package org.example.auth.Service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.example.auth.Mapper.LoginMapper;
import org.example.auth.POJO.BO.CheckEmailBO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final MailSender sender;

    public LoginServiceImpl(LoginMapper loginMapper, StringRedisTemplate stringRedisTemplate, MailSender sender) {
        this.loginMapper = loginMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.sender = sender;
    }
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        logger.info("现在进行登录,现在的传入的参数为:{}",loginDTO);
        loginDTO.setPassword(MD5Encryptor.encryptToMD5(loginDTO.getPassword()));
        logger.debug("完成MD5算法加密 ");
        List<LoginPO> loginPO = loginMapper.login(loginDTO);
        logger.info("完成密码账号比对,获取的数据为:{}", loginPO);
        return jwtMaker(loginPO);
    }

    private LoginVO jwtMaker(List<LoginPO> loginPO) {
        logger.debug("现在进入JWT令牌的加工环节");
        List<LoginPO> newLoginPO = loginPO.stream().filter(aloginPO -> aloginPO.getStatus() > 0).toList();
        logger.info("进行假删除过的账号的过滤工作,过滤后为:{}",newLoginPO);
        if (newLoginPO.isEmpty()) {
            logger.error("生成JWT令牌时，过滤后的获得的用户为空");
            throw new AppException(HttpStatus.BAD_REQUEST,"登录错误",null);
        }
        if(newLoginPO.size()>1){
         logger.error("唯一账号发生重复问题，为了防止出现问题仍然负载均衡进行登录");
        }
        LoginPO oneloginPO = newLoginPO.get(RandomUtil.randomInt(0, newLoginPO.size()-1));
        logger.debug("负载均衡获取到一个用户信息");
        Map<String,Object> map =new HashMap<>();
        logger.debug("创建Map集合");
        map.put("users", oneloginPO);
        logger.debug("将用户信息放入Map集合中");
        String jwt = JwtUtils.generateToken(map);
        logger.debug("完成JWT令牌加工");
        return new LoginVO(jwt);
    }

    @Override
    public void logout(String jwt) {
        logger.debug("登出操作，将JWT拉黑");
        stringRedisTemplate.opsForValue().set("blacklist:jwt"+jwt, "BLACK");
    }

    @Override
    public void register(RegisterDTO registerDTO, MultipartFile file) {
        logger.debug("现在进行注册操作");
        if(loginMapper.checkUnique(registerDTO.getPhone(),registerDTO.getEmail())!=0){
            logger.error("注册发生重复的问题");
            throw new AppException(HttpStatus.BAD_REQUEST,"手机号或邮箱已被注册",null);
        }
        logger.debug("完成发生验证码进行下面的操作");
        sendCode(registerDTO.getEmail(),"register");
        logger.debug("完成校验码的邮箱发送");
    }

    @Override
    public void mLogin(MLoginDTO mloginDTO) {
        logger.debug("现在进行邮箱登录操作");
        sendCode(mloginDTO.getEmail(), "code");
    }

    private void sendCode(String email, String type) {
        logger.debug("进行邮箱发送验证码");
        if (stringRedisTemplate.hasKey("email:code:"+email)) {
            logger.error("验证码发生过了");
            throw new AppException(HttpStatus.BAD_REQUEST,"验证码已发送",null);
        }
        SimpleMailMessage mail =new SimpleMailMessage();
        String code = RandomUtil.randomNumbers(6);
        logger.info("完成生成验证码:{}",code);
        stringRedisTemplate.opsForValue().set("email:"+ type +":"+email, code, 600, java.util.concurrent.TimeUnit.SECONDS);
        mail.setSubject("验证码");
        mail.setText("验证码为："+ code);
        mail.setTo(email);
        mail.setFrom("3221834658@qq.com");
        sender.send(mail);
        logger.debug("完成验证码的发送");
    }

    @Override
    public LoginVO LoginByCode(CheckEmailBO l) {
        CheckCode(l,"code");
        List<LoginPO> loginPO = loginMapper.LoginByCode(l.getEmail());
        return jwtMaker(loginPO);
    }

    private void CheckCode(CheckEmailBO l, String key) {
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
        CheckCode(new CheckEmailBO(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode()),"reset");
        resetPasswordDTO.setPassword(MD5Encryptor.encryptToMD5(resetPasswordDTO.getPassword()));
        ResetPO resetPO = new ResetPO(resetPasswordDTO.getEmail(), resetPasswordDTO.getPhone(),resetPasswordDTO.getPassword());
        loginMapper.ResetPassword(resetPO);
    }

    @Override
    public void registerOK(RegisterDTO registerDTO, MultipartFile file, String code) throws IOException {
        logger.debug("现在进行注册信息存入操作");
        CheckCode(new CheckEmailBO(registerDTO.getEmail(), code),"register");
        logger.debug("完成验证码的校验");
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
}
