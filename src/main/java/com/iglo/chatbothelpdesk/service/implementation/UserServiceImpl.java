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
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public UserResponse auth(AuthRequest request) {
        if (request.getPhone() == null && request.getTelegramId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Request Body");
        }

        User user = new User();
        if (request.getPhone() != null) {
            user = userRepository
                    .findFirstByPhone(request.getPhone())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Phone Number Not Registered"
                    ));
        } else if (request.getTelegramId() != null){
            user = userRepository
                    .findFirstByTelegramId(request.getTelegramId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Telegram Account Not Registered"
                    ));
        }

        return getUserResponse(user);
    }

    private static UserResponse getUserResponse(User user) {
        Company company = user.getCompany();
        return new UserResponse(
                user.getId(),
                user.getName(),
                company.getId(),
                company.getName()
        );
    }

    @Transactional
    @Override
    public UserResponse register(UserRequest request) {
        Company company = getCompany(request.getCompanyId());

        User user = new User();
        user.setCompany(company);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setTelegramId(request.getTelegramId());
        userRepository.save(user);

        return getUserResponse(user);
    }

    private Company getCompany(Long companyId) {
        return companyRepository
                .findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Company Not Found"
                ));
    }

}

