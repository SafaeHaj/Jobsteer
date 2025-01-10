package com.Jobsteer.Jobsteer.controllers;

import com.Jobsteer.Jobsteer.dto.UserUpdateDTO;
import com.Jobsteer.Jobsteer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, updateDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
