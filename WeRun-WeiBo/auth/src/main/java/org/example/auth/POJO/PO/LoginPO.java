package org.example.auth.POJO.PO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
public class LoginPO {

    private String uuid;
    private String Username;
    private String Phone;
    private String Email;
    private int Status;
    private String Gender;
    private String AvatarURL;
    private String Signature;
    private String Address;
    private Date Birthday;
    private Timestamp created_at;
    private Timestamp updated_at;
}
