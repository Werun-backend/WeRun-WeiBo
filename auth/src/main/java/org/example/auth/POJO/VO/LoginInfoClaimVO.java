package org.example.auth.POJO.VO;

import io.jsonwebtoken.Claims;

public record LoginInfoClaimVO(Claims claims,LoginVO loginVO) {
}
