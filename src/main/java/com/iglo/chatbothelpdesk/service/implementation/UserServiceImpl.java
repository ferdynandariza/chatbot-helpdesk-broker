package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.CompanyRepository;
import com.iglo.chatbothelpdesk.dao.UserRepository;
import com.iglo.chatbothelpdesk.entity.Company;
import com.iglo.chatbothelpdesk.entity.User;
import com.iglo.chatbothelpdesk.model.user.*;
import com.iglo.chatbothelpdesk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            CompanyRepository companyRepository)
    {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public UserResponse auth(AuthRequest request) {
        User user = userRepository
                .findFirstByPhone(request.getPhone())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Phone number not registered"
                ));

        return getUserResponse(user);
    }

    private static UserResponse getUserResponse(User user) {
        Company company = user.getCompany();
        return new UserResponse(
                user.getName(),
                company.getId(),
                company.getName()
        );
    }

    @Override
    public UserResponse register(UserRequest request) {
        Company company = getCompany(request.getCompanyId());

        User user = new User(
        null,
          company,
          request.getName(),
          request.getPhone()
        );
        userRepository.save(user);
        return getUserResponse(user);
    }

    private Company getCompany(Long companyId) {
        Company company = companyRepository
                .findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Company not found"
                ));
        return company;
    }

}
