package dev.pawan.sharemate.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.pawan.sharemate.model.User;


public class UserPrincipal implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7299259423367986391L;
	private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
    
    public Integer getUserId() {
		return this.user.getId();
	}

}
