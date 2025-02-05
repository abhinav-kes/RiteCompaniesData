package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.WebUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

        @GetMapping("/custom-login")
        public String login(){
                return "loginpage";
        }

        @GetMapping("/signup")
        public String signup(){
                return "loginpage";
        }

        @GetMapping("/home")
        public String home(Authentication authentication, Model model) {

                if (authentication != null) {
                        // Get the principal (this will be a WebUser object if you've set it up correctly)
                        Object principal = authentication.getPrincipal();
                        System.out.println(principal.toString());
                        if (principal instanceof WebUser) {
                                WebUser loggedInUser = (WebUser) principal;
                                String username = loggedInUser.getUsername();
                                String email = loggedInUser.getEmail();

                                // Adding user details to the model
                                model.addAttribute("username", username);
                                model.addAttribute("email", email);

                        }
                }
                return "home";
        }

}
