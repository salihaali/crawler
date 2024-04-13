package com.sally.webcrawler.config;

import com.sally.webcrawler.entity.MyUser;
import com.sally.webcrawler.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepository myUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = myUserRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Does Not Exist");
        }

        return new MyUserDetails(user.get());
    }
}
