package com.cgs.authservice.serviceimpl;


import com.cgs.authservice.model.Users;
import com.cgs.authservice.model.dto.LoginRequestDto;
import com.cgs.authservice.repository.UserRepo;
import com.cgs.authservice.security.AppRoles;
import com.cgs.authservice.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserServicesImpl implements UserServices, UserDetailsService {

    @Autowired
    UserRepo userRepo;


    @Override
    public Users loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users userBean = userRepo.findByUserName(userName);
        if (userBean == null) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        /*Todo Get User role and add on grand authority */
//        AppRoles appRoles = AppRoles.valueOf(userBean.getRole());/*User ROle Maker or Checker or MakerCheckerRole*/
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", "ADMIN")));
        userBean.setRoles(authorities);
        return userBean;
    }


    @Override
    public Users findUser(LoginRequestDto data) {
        return loadUserByUsername(data.getUserName());
    }
}
