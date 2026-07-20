package com.campus.issue_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnLandingPage() throws Exception {
        String content = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("LANDING PAGE CONTENT START");
        System.out.println(content);
        System.out.println("LANDING PAGE CONTENT END");
    }
}
