package com.detection.userservice.service;

import com.detection.userservice.entity.Contact;
import com.detection.userservice.entity.User;
import com.detection.userservice.repository.ContactRepository;
import com.detection.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    public Map<String, Object> loginUser(String uname, String pwd) {
        User user = userRepository.findByUserNameAndPassword(uname, pwd);
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("userId", user.getId());
        } else {
            response.put("success", false);
            response.put("message", "UserName/Password is Invalid");
        }
        return response;
    }

    public Map<String, Object> registerUser(String fname, String lname, String uname,
                                            String pwd, String email, String phnum, String address) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName(fname);
        user.setLastName(lname);
        user.setUserName(uname);
        user.setPassword(pwd);
        user.setEmailId(email);
        user.setPhoneNumber(phnum);
        user.setAddress(address);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    public Optional<User> getUserProfile(String id) {
        return userRepository.findById(id);
    }

    public Map<String, Object> saveContact(String name, String email, String subject, String message) {
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setContactName(name);
        contact.setEmailId(email);
        contact.setSubject(subject);
        contact.setMessage(message);
        contactRepository.save(contact);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
}
