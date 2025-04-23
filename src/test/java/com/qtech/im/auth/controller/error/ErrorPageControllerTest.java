package com.qtech.im.auth.controller.error;

import com.nimbusds.jose.util.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/18 10:06:27
 * desc   :
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRender404Template() throws Exception {
        mockMvc.perform(get("/error/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("404")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("页面走丢了")));
    }

    @Test
    public void testRenderGenericErrorTemplate() throws Exception {
        mockMvc.perform(get("/error/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("系统发生未知错误")));
    }
}
