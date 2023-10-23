package com.iglo.chatbothelpdesk.adapter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import com.iglo.chatbothelpdesk.model.dolphin.BlastingWaResponse;
import com.iglo.chatbothelpdesk.model.dolphin.BroadcastRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class BlastingAdapter {

    @Value("${sdk.dolphin.base.url}")
    String dolphinBaseUrl;

    @Value("${sdk.dolphin.graph.broadcast.wa}")
    String dolphinBroadcastWa;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final DolphinAuthAdapter dolphinAuthAdapter;

    public BlastingAdapter(DolphinAuthAdapter dolphinAuthAdapter) {
        this.dolphinAuthAdapter = dolphinAuthAdapter;
    }

    public ResponseEntity<BlastingWaResponse> blasting(BlastingTemplate template) {
        String token = dolphinAuthAdapter.getToken();
        return blasting(template, token);
    }

    public ResponseEntity<BlastingWaResponse> blasting(BlastingTemplate template, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth(token);
        log.info("Dolphin Token: {}", token);

        BroadcastRequest requestBody = createRequestBody(template);
        log.info("Body String: {}", requestBody);

        HttpEntity<BroadcastRequest> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<BlastingWaResponse> response = restTemplate.exchange(
                    dolphinBaseUrl + dolphinBroadcastWa, HttpMethod.POST, entity, BlastingWaResponse.class
            );
            return response;
        } catch (RestClientResponseException e){
             log.error("{}", e.getMessage());
            return new ResponseEntity<>(
                    e.getResponseBodyAs(BlastingWaResponse.class),
                    null,
                    e.getStatusCode());
        }
    }


    private BroadcastRequest createRequestBody(BlastingTemplate template){
        Map<String, String> fields = convertFields(template);

        BroadcastRequest request = new BroadcastRequest();
        request.setName(template.getName());
        request.setChannel(template.getChannel());
        request.setSendingMode("Now");
        request.setTemplateId(template.getTemplateId());
        request.setHeaderVariables(List.of());
        request.setContent(List.of());
        request.setBodyVariables(createBodyVariables(fields));
        request.setWhatsappRecipient(createWhatsappRecipient(template.getPhone(), fields));
        return request;
    }

    private static Map<String, String> convertFields(BlastingTemplate template) {
        Map<String, String> fields;
        try {
            fields = objectMapper.readValue(template.getFields(), LinkedHashMap.class);
        } catch (Exception ignored){
            fields = Map.of();
        }
        return fields;
    }

    private static List<Map<String, String>> createWhatsappRecipient(String phone, Map<String, String> fields) {
        Map<String, String> whatsappRecipient = new LinkedHashMap<>();
        whatsappRecipient.put("Recipient", phone);
        whatsappRecipient.putAll(fields);
        return List.of(whatsappRecipient);
    }

    private static List<BroadcastRequest.BodyVariable> createBodyVariables(Map<String, String> fields) {
        List<BroadcastRequest.BodyVariable> bodyVariables = new ArrayList<>();
        int index = 1;
        for (var field : fields.entrySet()) {
            String key = String.format("{{%d}}", index);
            String value = field.getKey();
            bodyVariables.add(new BroadcastRequest.BodyVariable(key, value));
            index++;
        }
        return bodyVariables;
    }
/*
        private String createBlastingRequestBody(BlastingTemplate template) {
        Map<String, Object> fields = readFieldsFromString(template.getFields());
        Map<String, Object> mapOfRequestBody = createMapOfRequestBody(template, fields);
        try {
            String stringBody = objectMapper.writeValueAsString(mapOfRequestBody);
            log.info(stringBody);
            return stringBody;
        } catch (JsonProcessingException e){
            log.error(e.getMessage());
        }
        return "{}";
    }

    private Map<String, Object> createMapOfRequestBody(BlastingTemplate template, Map<String, Object> fields) {
        Map<String, Object> mapOfRequestBody = new LinkedHashMap<>();
        mapOfRequestBody.put("name", template.getName());
        mapOfRequestBody.put("channel", template.getChannel());
        mapOfRequestBody.put("sendingMode", "Now");
        mapOfRequestBody.put("templateId", template.getTemplateId());
        mapOfRequestBody.put("headerVariables", List.of());
        mapOfRequestBody.put("bodyVariables", createBodyVariable(fields));
        mapOfRequestBody.put("content", List.of());
        mapOfRequestBody.put("whatsappRecipient", createWhatsappRecipient(template.getPhone(), fields));
        return mapOfRequestBody;
    }

    private static ArrayNode createWhatsappRecipient(String phone, Map<String, Object> fields) {
        ArrayNode whatsappRecipient = objectMapper.createArrayNode();
        fields.put("Recipient", phone);
        whatsappRecipient.add(objectMapper.convertValue(fields, JsonNode.class));
        return whatsappRecipient;
    }

    private Map<String, Object> readFieldsFromString(String templateFields) {
        Map<String, Object> fields = new LinkedHashMap<>();
        try {
        fields = objectMapper.readValue(templateFields, Map.class);
        } catch (Exception e) {
        log.error(e.getMessage());
        }
        return fields;
    }

    private ArrayNode createBodyVariable(Map<String, Object> fields) {
        int fieldIndex = 1;
        ArrayNode bodyVariables = objectMapper.createArrayNode();
        for (Map.Entry<String, Object> data : fields.entrySet()) {
            Map<String, Object> bodyVariable = new LinkedHashMap<>();
            bodyVariable.put("type", "personalized");
            bodyVariable.put("key", String.format("{{%d}}", fieldIndex));
            bodyVariable.put("value", data.getKey());
            bodyVariables.add(objectMapper.convertValue(bodyVariable, JsonNode.class));
            fieldIndex += 1;
        }
        return bodyVariables;
    }*/

}
