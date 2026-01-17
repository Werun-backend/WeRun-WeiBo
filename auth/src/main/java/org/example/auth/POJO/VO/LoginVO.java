package org.example.auth.POJO.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32218
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    //token
    private String token;
    //用于刷新token
    private String refreshToken;
}
