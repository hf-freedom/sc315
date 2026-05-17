package com.vaccine.service;

import com.vaccine.dao.DataStore;
import com.vaccine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private DataStore dataStore;

    public User createUser(User user) {
        user.setId(dataStore.userIdGen.getAndIncrement());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        dataStore.users.put(user.getId(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(dataStore.users.values());
    }

    public User getUserById(Long id) {
        return dataStore.users.get(id);
    }

    public User getUserByIdCard(String idCard) {
        return dataStore.users.values().stream()
                .filter(u -> u.getIdCard().equals(idCard))
                .findFirst().orElse(null);
    }
}
