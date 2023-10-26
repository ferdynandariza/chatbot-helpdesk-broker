package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.company.CompanyRequest;
import com.iglo.chatbothelpdesk.model.company.CompanyResponse;

import java.util.List;

public interface CompanyService {

    CompanyResponse insert(CompanyRequest request);
    CompanyResponse get(Long companyId);
    List<CompanyResponse> getCompanies();
}
