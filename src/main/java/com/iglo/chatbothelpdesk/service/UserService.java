package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.user.AuthRequest;
import com.iglo.chatbothelpdesk.model.user.UserRequest;
import com.iglo.chatbothelpdesk.model.user.UserResponse;

public interface UserService {

    UserResponse auth(AuthRequest request);

    UserResponse register(UserRequest request);

}
