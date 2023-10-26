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

}
