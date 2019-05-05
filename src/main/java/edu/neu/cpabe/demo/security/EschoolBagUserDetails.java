package edu.neu.cpabe.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户信息
 */
public class EschoolBagUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 3679731990944546341L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(getRole());
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    public EschoolBagUserDetails(User user) {
        super(user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.getStudent(), user.getTeacher(), user.getAccountNonExpired(),
                user.getAccountNonLocked(), user.getCredentialsNonExpired(), user.getEnabled());
    }

}
