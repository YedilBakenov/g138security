package com.security.securityg138.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/")
    public String main(){
        return "index";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/sign-in")
    public String signIn(){
        return "sign";
    }

    @GetMapping(value = "/forbidden")
    public String forbiddenPage(){
        return "forbidden";
    }
}
