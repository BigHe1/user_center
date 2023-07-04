package com.lin.user_center;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
        Calendar calendar = Calendar.getInstance();
        final String SALT = calendar.getTime().toString();
        String newPassword = DigestUtils.md5DigestAsHex((SALT + "123")
                .getBytes(StandardCharsets.UTF_8));
        System.out.println(newPassword);
    }

}
