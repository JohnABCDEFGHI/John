package com.gyh.Hamburger;

import com.gyh.Hamburger.common.EmailMessageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HamburgerApplicationTests {
    @Autowired
    EmailMessageUtil util;
    @Test
    void contextLoads() {



        util.sendMessage("2864562395@qq.com",EmailMessageUtil.achieveCode());

    }

}
