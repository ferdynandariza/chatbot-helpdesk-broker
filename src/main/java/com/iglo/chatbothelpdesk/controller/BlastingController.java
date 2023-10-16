package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.blasting.BlastingTemplateRequest;
import com.iglo.chatbothelpdesk.service.BlastingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blasting")
public class BlastingController {

    private final BlastingService blastingService;

    public BlastingController(BlastingService blastingService) {
        this.blastingService = blastingService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> sendBlasting(@RequestBody BlastingTemplateRequest request){
        String response = blastingService.send(request);
        return WebResponse.<String>builder().data(response).build();
    }

}
