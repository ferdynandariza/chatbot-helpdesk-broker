package com.iglo.chatbothelpdesk.adapter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import com.iglo.chatbothelpdesk.model.blasting.BlastingWaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BlastingAdapter {

    @Value("${sdk.dolphin.base.url}")
    String dolphinBaseUrl;

    @Value("${sdk.dolphin.graph.broadcast.wa}")
    String dolphinBroadcastWa;

    private final DolphinAuthAdapter dolphinAuthAdapter;

    public BlastingAdapter(DolphinAuthAdapter dolphinAuthAdapter) {
        this.dolphinAuthAdapter = dolphinAuthAdapter;
    }

    public ResponseEntity<BlastingWaResponse> blasting(BlastingTemplate template) {
        RestTemplate restTemplate = new RestTemplate();
        String body = CreateObjectToSend(template);
        String token = dolphinAuthAdapter.getToken();
        log.info("Dolphin Token: " + token);
        log.info("Body String: " + body);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(dolphinBaseUrl + dolphinBroadcastWa, HttpMethod.POST,
                entity, BlastingWaResponse.class);
    }

    public ResponseEntity<BlastingWaResponse> blasting(BlastingTemplate template, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String body = CreateObjectToSend(template);
        log.info("Dolphin Token: " + token);
        log.info("Body String: " + body);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(dolphinBaseUrl + dolphinBroadcastWa, HttpMethod.POST,
                entity, BlastingWaResponse.class);
    }

    private String CreateObjectToSend(BlastingTemplate template) {
        int i = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        String objectToSend = "";
        Map<String, Object> objectToSendMap = new LinkedHashMap<>();
        Map<String, Object> bodyVariablesMap = new LinkedHashMap<>();
        List<String> content = new ArrayList<>();
        ArrayNode whatsappRecipientArray = objectMapper.createArrayNode();
        ArrayNode bodyVariablesArray = objectMapper.createArrayNode();
        try {
            Map<String, Object> fields = objectMapper.readValue(template.getFields(), Map.class);
            for (Map.Entry<String, Object> data : fields.entrySet()) {
                i += 1;
                bodyVariablesMap.put("key", String.format("{{%d}}", i));
                bodyVariablesMap.put("value", data.getKey());
                bodyVariablesArray.add(objectMapper.convertValue(bodyVariablesMap, JsonNode.class));
            }
            fields.put("Recipient", template.getPhone());
            whatsappRecipientArray.add(objectMapper.convertValue(fields, JsonNode.class));
            objectToSendMap.put("name", template.getName());
            objectToSendMap.put("channel", template.getChannel());
            objectToSendMap.put("sendingMode", "Now");
            objectToSendMap.put("templateId", template.getTemplateId());
            objectToSendMap.put("bodyVariables", bodyVariablesArray);
            objectToSendMap.put("content", content);
            objectToSendMap.put("whatsappRecipient", whatsappRecipientArray);
            objectToSend = objectMapper.writeValueAsString(objectToSendMap);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return objectToSend;
    }
}
