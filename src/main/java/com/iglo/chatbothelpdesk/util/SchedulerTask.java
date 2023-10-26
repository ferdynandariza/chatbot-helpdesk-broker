package com.iglo.chatbothelpdesk.util;

import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import com.iglo.chatbothelpdesk.service.BlastingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class SchedulerTask {

    private final BlastingService blastingService;

    public SchedulerTask(BlastingService blastingService) {
        this.blastingService = blastingService;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(cron = "*/10 * * * * *")
    public void deleteBlasting(){
        BlastingTemplate blastingTemplate = blastingService.getBlastingTemplate();
        if(blastingTemplate != null){
            log.info(blastingTemplate.toString());
        }
    }
}
