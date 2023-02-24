//package com.cgs.authentication.security;
//
//import com.cgs.authentication.model.Users;
//import com.cgs.authentication.repository.UserRepo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsPasswordService;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class DatabaseUserDetailPasswordService implements UserDetailsPasswordService {
//
//    private final UserRepo userRepository;
//
//    @Override
//    public UserDetails updatePassword(UserDetails user, String newPassword) {
//        log.info("User Password Update");
//        Users userCredentials =
//                userRepository.findByUserName(user.getUsername());
//        userCredentials.setUserPassword(newPassword);
//        log.info("User Password Updated ...");
//        return userCredentials;
//    }
//}
