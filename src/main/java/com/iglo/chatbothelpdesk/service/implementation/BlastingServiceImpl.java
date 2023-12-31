package com.iglo.chatbothelpdesk.service.implementation;

import com.google.gson.Gson;
import com.iglo.chatbothelpdesk.adapter.BlastingAdapter;
import com.iglo.chatbothelpdesk.dao.BlastingTemplateRepository;
import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import com.iglo.chatbothelpdesk.model.blasting.BlastingTemplateRequest;
import com.iglo.chatbothelpdesk.model.dolphin.BlastingWaResponse;
import com.iglo.chatbothelpdesk.service.BlastingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class BlastingServiceImpl implements BlastingService {

    private final BlastingAdapter blastingAdapter;

    private final BlastingTemplateRepository blastingTemplateRepository;

    public BlastingServiceImpl(BlastingAdapter blastingAdapter,
                               BlastingTemplateRepository blastingTemplateRepository) {
        this.blastingAdapter = blastingAdapter;
        this.blastingTemplateRepository = blastingTemplateRepository;
    }

    @Transactional
    @Override
    public String send(BlastingTemplateRequest request) {
        BlastingTemplate template = new BlastingTemplate();
        template.setName(request.getName());
        template.setChannel(request.getChannel());
        template.setTemplateId(request.getTemplateId());
        template.setPhone(request.getPhone());
        template.setSendCount(1);
        log.info(request.toString());
        if (request.getFields().size() != 0) {
            template.setFields(new Gson().toJson(request.getFields().get(0)));
        } else {
            template.setFields("{}");
        }

        ResponseEntity<BlastingWaResponse> response = blastingAdapter.blasting(template);
        if (response.getBody() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed hit blasting");

        log.info(response.getBody().getStatus());
        if (response.getBody().getStatus().equalsIgnoreCase("success")) {
//            blastingTemplateRepository.save(template);
            return "Successfully";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "[3Dolphin]: " + response.getBody().getMessage());
    }

    @Transactional
    @Override
    public BlastingTemplate getBlastingTemplate() {
        List<BlastingTemplate> templateList = blastingTemplateRepository.findAll();
        if (templateList.isEmpty()) {
            return null;
        } else {
            blastingTemplateRepository.delete(templateList.get(0));
            return templateList.get(0);
        }
    }


}
