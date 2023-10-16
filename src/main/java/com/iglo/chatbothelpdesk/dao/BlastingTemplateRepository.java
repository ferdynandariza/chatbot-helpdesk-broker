package com.iglo.chatbothelpdesk.dao;

import com.iglo.chatbothelpdesk.entity.BlastingTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlastingTemplateRepository extends JpaRepository<BlastingTemplate, Long> {
}
