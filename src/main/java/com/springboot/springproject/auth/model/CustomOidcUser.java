package com.springboot.springproject.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOidcUser implements OidcUser {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    private final String provider;

    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          Map<String, Object> attributes,
                          String nameAttributeKey,
                          String email,
                          String provider,
                          OidcIdToken idToken,
                          OidcUserInfo userInfo) {
        this.authorities = authorities;
        this.attributes = new HashMap<>(attributes);
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.provider = provider;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("email", email);
        newAttributes.put("provider", provider);
        return newAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        Object name = attributes.get(nameAttributeKey);
        return name != null ? name.toString() : email;
    }

    @Override
    public Map<String, Object> getClaims() {
        return attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }
}
