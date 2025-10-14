package com.springboot.springproject.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    private final String provider;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String email,
                            String provider) {
        this.authorities = authorities;
        this.attributes = new HashMap<>(attributes);
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        // 기존 attributes에 email을 넣어서 반환
        attributes.put("email", email);
        attributes.put("provider", provider);

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return (String) attributes.get(nameAttributeKey);
    }

    public String getEmail() {
        return email;
    }

    public String getProvider(){
        return provider;
    }
}
