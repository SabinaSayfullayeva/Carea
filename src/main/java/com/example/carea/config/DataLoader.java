package com.example.carea.config;

import com.example.carea.entity.Role;
import com.example.carea.entity.User;
import com.example.carea.entity.UserRole;
import com.example.carea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@RequiredArgsConstructor

@Component
public class DataLoader implements CommandLineRunner
{

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${photo.upload.path}")
    String photoPath;


    @Override
    public void run(String... args) throws Exception
    {
        createPhotoPath();
        securityUser();
    }

    private void securityUser()
    {
        String s = "admin@gmail.com";

        if (!userRepository.existsByEmail(s))
        {
            User user = new User();
            user.setRole(new UserRole(Role.ADMIN));
            user.setUsername("admin");
            user.setEmail(s);
            user.setPassword(passwordEncoder.encode("x*x=-1"));
            userRepository.save(user);
        }

    }



    private void createPhotoPath()
    {
        File directory = new File(photoPath);
        if (!directory.exists())
            directory.mkdirs();
    }

}
