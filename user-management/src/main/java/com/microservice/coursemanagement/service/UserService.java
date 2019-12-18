package com.microservice.coursemanagement.service;

import com.microservice.coursemanagement.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User findByUsername(String username);

    List<String> findUsers(List<Long> idList);
}
