package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.WebUser;
import com.example.DataDisplay.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping(value="/signup",consumes="application/json")
    public WebUser createUser(@RequestBody WebUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return webUserRepository.save(user);
    }

//    @GetMapping("/user-details")
//    public String userDetails(Model model) {
//        // Add attributes to the model (these values can come from a service or session)
//        model.addAttribute("username", "JohnDoe");
//        model.addAttribute("email", "johndoe@example.com");
//        return "userDetails"; // Name of the Thymeleaf template
//    }
}
