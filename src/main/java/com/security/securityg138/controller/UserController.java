package com.security.securityg138.controller;

import com.security.securityg138.model.User;
import com.security.securityg138.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/")
    public String main(){
        System.out.println(userService.getCurrentUser().getFullName() + "/" +
                userService.getCurrentUser().getEmail());
        return "index";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/sign-in")
    public String signIn(){
        return "sign";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/forbidden")
    public String forbiddenPage(){
        return "forbidden";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    public String adminPage(){
        return "admin";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/register")
    public String getRegisterPage(){
        return "register-page";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public String addUser(User user, @RequestParam String rePassword){
        String result =  userService.addUser(user, rePassword);

        if(result.equals("success")) {
            return "index";
        }else return "sign";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/change-pass")
    public String changePassword(@RequestParam String oldPass,
                                 @RequestParam String newPass,
                                 @RequestParam String reNewPass){
        userService.changePassword(oldPass, newPass, reNewPass);

        return "redirect:/";
    }

}
