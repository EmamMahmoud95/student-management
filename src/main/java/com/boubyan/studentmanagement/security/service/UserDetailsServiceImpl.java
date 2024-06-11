package com.boubyan.studentmanagement.security.service;

import com.boubyan.studentmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository studentRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = studentRepository.findUserByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with email: " + username);
        }
        return UserDetailsImpl.build(user);
    }
}
