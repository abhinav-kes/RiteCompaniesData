package com.example.DataDisplay.service;

import com.example.DataDisplay.model.WebUser;
import com.example.DataDisplay.repository.WebUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WebUserService implements UserDetailsService {
    @Autowired
    private WebUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<WebUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            String email=userObj.getEmail();
            String password=userObj.getPassword();
            return new WebUser(username, password, email);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

}
