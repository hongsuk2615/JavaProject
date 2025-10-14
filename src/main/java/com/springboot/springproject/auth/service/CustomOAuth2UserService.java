package com.springboot.springproject.auth.service;

import com.springboot.springproject.auth.jwt.JwtProvider;
import com.springboot.springproject.auth.model.CustomOAuth2User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

    private final JwtProvider jwtProvider;

    public CustomOAuth2UserService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email"); // github일경우 null
        String provider = userRequest.getClientRegistration().getRegistrationId(); // oauth 공급ID (google, github)


        if(provider.equals("github")) {
            // 이메일이 없으면 API 호출
            String token = userRequest.getAccessToken().getTokenValue();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "token " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    (Class<List<Map<String, Object>>>) (Class) List.class
            );
            List<Map<String, Object>> emails = response.getBody();

            if (emails != null && !emails.isEmpty()) {
                email = (String) emails.get(0).get("email");
            }
        }

        // JWT 생성 시 email 포함
        return new CustomOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "login",
                email,
                provider
        );
    }
}
