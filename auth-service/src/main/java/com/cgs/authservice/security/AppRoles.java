package com.cgs.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public enum AppRoles {

    MAKER(new HashSet<>(Arrays.asList(AppPermissions.READ))),
    CHECKER(new HashSet<>(Arrays.asList(AppPermissions.WRITE))),
    MAKER_CHECKER(new HashSet<>(Arrays.asList(AppPermissions.READ,AppPermissions.WRITE)));

    Set<AppPermissions> permissions = new HashSet<>();

    AppRoles(Set<AppPermissions> permissions) {
        this.permissions=permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        log.info("Granted Authority for " + this.name() + " - " + this.permissions.stream().map(p -> new SimpleGrantedAuthority(p.name())).collect(Collectors.toSet()).toString());
        return this.permissions.stream().map(p -> new SimpleGrantedAuthority(p.name())).collect(Collectors.toSet());
    }
}
