package org.example.auth.POJO.VO;

import org.example.common.model.user.UserBO;

public record LoginInfVO(LoginVO loginVO, UserBO userBO) {
}
