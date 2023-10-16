package com.iglo.chatbothelpdesk.adapter;

import com.iglo.chatbothelpdesk.model.dolphin.DolphinAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DolphinAuthAdapter {

    @Value("${sdk.dolphin.base.url}")
    String dolphinBaseUrl;

    @Value("${sdk.dolphin.graph.auth}")
    String dolphinAuth;

    @Value("${sdk.username}")
    String username;

    @Value("${sdk.password}")
    String password;

    public String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> body = new LinkedHashMap<>();
        body.put("username", username);
        body.put("password", password);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        DolphinAuthResponse strToken = restTemplate
                .exchange(dolphinBaseUrl + dolphinAuth, HttpMethod.POST, entity, DolphinAuthResponse.class)
                .getBody();

        return (strToken != null) ? strToken.getToken() : "";
    }

}
