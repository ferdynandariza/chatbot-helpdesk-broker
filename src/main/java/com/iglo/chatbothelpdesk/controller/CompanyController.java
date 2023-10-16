package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.company.CompanyRequest;
import com.iglo.chatbothelpdesk.model.company.CompanyResponse;
import com.iglo.chatbothelpdesk.service.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CompanyResponse> register(@RequestBody CompanyRequest request){
        CompanyResponse response = companyService.insert(request);
        return WebResponse.<CompanyResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/{companyId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CompanyResponse> get(@PathVariable(name = "companyId") Long companyId){
        CompanyResponse response = companyService.get(companyId);
        return WebResponse.<CompanyResponse>builder().data(response).build();
    }


}
