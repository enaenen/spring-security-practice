package me.whiteship.demospringsecurityform.account;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest{
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

//어노테이션 버전(위와 동일)
    @Test
    @WithAnonymousUser
    public void index_anonymous_annotation() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }




    @Test
    public void index_user() throws Exception {
        mockMvc.perform(get("/").with(user("space").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_admin() throws Exception {
        mockMvc.perform(get("/").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin").with(user("space").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }


//Annotation
    @Test
    @WithMockUser(username="space", roles="USER")
    public void index_user_annotation() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //CustomAnnotation
    @Test
    @WithUser
    public void index_user_custom_annotation() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    //폼 로그인 TEST
    @Test
    @Transactional
    public void login_success() throws Exception {
        String username = "test";
        String password = "123";
        Account user = createUser(username,password);
        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
        .andExpect(authenticated());
    }

    private Account createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        return accountService.createNew(account);
    }


    @Test
    @Transactional
    //Transactional을 통해 RollBack을 해준다
    public void login_fail() throws Exception {
        String username = "test";
        String password = "123";
        Account user = createUser(username,password);
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345"))
                .andExpect(unauthenticated());
    }

}
