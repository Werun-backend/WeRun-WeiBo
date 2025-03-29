package org.example.auth.POJO.PO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterPO {
    private String Username;
    private String Phone;
    private String Password;
    private String Gender;
    private String AvatarURL;
    private String uuid;
}
