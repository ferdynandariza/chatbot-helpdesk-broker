package com.iglo.chatbothelpdesk.dao;

import com.iglo.chatbothelpdesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByPhone(String phone);

}
