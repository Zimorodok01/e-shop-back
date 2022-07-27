package com.example.eshopback.model.enums;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Set;
import java.util.stream.Collectors;

import static com.example.eshopback.model.enums.Privilege.*;

@Getter
@AllArgsConstructor
public enum Role {
    MANAGER(Sets.newHashSet(
            SELLER_WRITE,
            SELLER_READ,
            POINT_WRITE,
            POINT_READ,
            PRODUCT_WRITE,
            PRODUCT_READ,
            ORDER_CREATE,
            ORDER_READ,
            SUPPLY_READ,
            SUPPLY_WRITE
    )),
    SELLER(Sets.newHashSet(
            PRODUCT_READ,
            ORDER_CREATE,
            ORDER_READ
    ));

    private final Set<Privilege> privileges;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPrivileges().parallelStream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
