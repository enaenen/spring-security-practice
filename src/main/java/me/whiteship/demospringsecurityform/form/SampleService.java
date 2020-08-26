package me.whiteship.demospringsecurityform.form;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {
    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();;
        //사실상 UserDetails 타입
        Object principal = authentication.getPrincipal();
        //사용자가 가진 권한
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    }
}
