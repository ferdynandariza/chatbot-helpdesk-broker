package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import com.iglo.chatbothelpdesk.model.blasting.BlastingTemplateRequest;

public interface BlastingService {

    String send(BlastingTemplateRequest request);

    BlastingTemplate getBlastingTemplate();
}
