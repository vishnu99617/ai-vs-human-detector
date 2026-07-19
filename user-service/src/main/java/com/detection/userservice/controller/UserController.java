package com.detection.userservice.controller;

import com.detection.userservice.entity.User;
import com.detection.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String uname, @RequestParam String pwd) {
        Map<String, Object> response = userService.loginUser(uname, pwd);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addnewuser")
    public ResponseEntity<Map<String, Object>> addnewuser(
            @RequestParam String fname, @RequestParam String lname,
            @RequestParam String uname, @RequestParam String pwd,
            @RequestParam String email, @RequestParam String phnum,
            @RequestParam String address) {
        
        Map<String, Object> response = userService.registerUser(fname, lname, uname, pwd, email, phnum, address);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable String id) {
        return userService.getUserProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, Object>> contact(
            @RequestParam String name, @RequestParam String email,
            @RequestParam String subject, @RequestParam String message) {
        
        Map<String, Object> response = userService.saveContact(name, email, subject, message);
        return ResponseEntity.ok(response);
    }
}
