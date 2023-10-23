package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.CompanyRepository;
import com.iglo.chatbothelpdesk.entity.Company;
import com.iglo.chatbothelpdesk.model.company.CompanyRequest;
import com.iglo.chatbothelpdesk.model.company.CompanyResponse;
import com.iglo.chatbothelpdesk.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    @Override
    public CompanyResponse insert(CompanyRequest request) {
        Company company = new Company();
        company.setName(request.getName());
        company = companyRepository.save(company);
        return new CompanyResponse(company.getId(), company.getName());
    }

    @Transactional
    @Override
    public CompanyResponse get(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Company not found"
        ));
        return new CompanyResponse(company.getId(), company.getName());
    }

}
