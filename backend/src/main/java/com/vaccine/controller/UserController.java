package com.vaccine.controller;

import com.vaccine.common.Result;
import com.vaccine.entity.User;
import com.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Result<User> create(@RequestBody User user) {
        return Result.success(userService.createUser(user));
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @GetMapping("/idcard/{idCard}")
    public Result<User> getByIdCard(@PathVariable String idCard) {
        return Result.success(userService.getUserByIdCard(idCard));
    }
}
