package com.AyushToCode.JobPortalApplication.Controller;
import com.AyushToCode.JobPortalApplication.entity.Users;
import com.AyushToCode.JobPortalApplication.entity.UsersType;
import com.AyushToCode.JobPortalApplication.services.UserTypeService;
import com.AyushToCode.JobPortalApplication.services.UsersService;
import com.AyushToCode.JobPortalApplication.services.UserTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UserTypeService userTypeService;
    private final UsersService usersService;

    @Autowired
    public UsersController(UserTypeService userTypeService, UsersService usersService) {
        this.userTypeService = userTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersType=userTypeService.getAll();
        model.addAttribute("getAllTypes",usersType);
        model.addAttribute("user" , new Users());
        System.out.println(model.getAttribute("error"));
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model, RedirectAttributes redirectAttributes){
        Optional<Users> byEmail = usersService.findByEmail(users.getEmail());
        if(byEmail.isPresent()){
            redirectAttributes.addFlashAttribute("error", "Email already registered, try to login with another email.");
            return "redirect:/register";
        }
        usersService.adduser(users);
        return "login";

    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";

    }
}
