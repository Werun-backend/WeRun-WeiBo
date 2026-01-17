package org.example.auth.POJO.VO;

import org.example.common.model.user.UserBO;

/**
 * 用于封装返回的数据的
 * @param loginVO 校验实体类
 * @param userBO 用户实体类
 */
public record LoginInfVO(
        LoginVO loginVO,
        UserBO userBO) {
}
