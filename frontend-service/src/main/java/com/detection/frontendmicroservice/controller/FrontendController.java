package com.detection.frontendmicroservice.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class FrontendController {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "http://api-gateway";

    @GetMapping("/")
    public String homepage() { return "index"; }

    @GetMapping("/index")
    public String indexpage() { return "index"; }

    @GetMapping("/logout")
    public String logoutpage(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/about")
    public String aboutpage() { return "about"; }

    @GetMapping("/usermainpage")
    public String usermainpage() { return "usermainpage"; }

    @GetMapping("/services")
    public String servicespage() { return "services"; }

    @GetMapping("/gallery")
    public String gallerypage() { return "gallery"; }

    @GetMapping("/userlogin")
    public String userloginpage() { return "userlogin"; }

    @PostMapping("/userlogincheck")
    public String userlogincheck(@RequestParam String uname, @RequestParam String pwd, HttpSession session, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("uname", uname);
        map.add("pwd", pwd);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    GATEWAY_URL + "/api/user/login", org.springframework.http.HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> body = response.getBody();
            if (body != null && Boolean.TRUE.equals(body.get("success"))) {
                session.setAttribute("userid", body.get("userId"));
                return "usermainpage";
            } else {
                model.addAttribute("msg", "UserName/Password is Invalid");
                return "userlogin";
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Error connecting to user service: " + e.getMessage());
            return "userlogin";
        }
    }

    @GetMapping("/newuser")
    public String newuser(Model model) {
        model.addAttribute("msg", "");
        return "newuser";
    }

    @PostMapping("/addnewuser")
    public String addnewuser(@RequestParam String fname, @RequestParam String lname,
                             @RequestParam String uname, @RequestParam String pwd,
                             @RequestParam String email, @RequestParam String phnum,
                             @RequestParam String address, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("fname", fname); map.add("lname", lname); map.add("uname", uname);
        map.add("pwd", pwd); map.add("email", email); map.add("phnum", phnum);
        map.add("address", address);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        try {
            restTemplate.postForEntity(GATEWAY_URL + "/api/user/addnewuser", request, Map.class);
            model.addAttribute("msg", "New User Added Success");
        } catch (Exception e) {
            model.addAttribute("msg", "Error connecting to user service: " + e.getMessage());
        }
        return "newuser";
    }

    @GetMapping("/contact")
    public String contactpage() { return "contact"; }

    @PostMapping("/contact")
    public String contactpageSubmit(@RequestParam String name, @RequestParam String email,
                                    @RequestParam String subject, @RequestParam String message, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", name); map.add("email", email); map.add("subject", subject); map.add("message", message);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        try {
            restTemplate.postForEntity(GATEWAY_URL + "/api/user/contact", request, Map.class);
            model.addAttribute("msg", "Contact Added Success");
        } catch (Exception e) {
            model.addAttribute("msg", "Error connecting to user service: " + e.getMessage());
        }
        return "contact";
    }

    @GetMapping("/userviewprofile")
    public String userviewprofile(HttpSession session, Model model) {
        String id = (String) session.getAttribute("userid");
        if (id == null) return "index";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(GATEWAY_URL + "/api/user/profile/" + id, Map.class);
            model.addAttribute("data", response.getBody());
            return "userviewprofile";
        } catch (Exception e) {
            model.addAttribute("msg", "Error connecting to user service: " + e.getMessage());
            return "userviewprofile";
        }
    }

    @GetMapping("/detector")
    public String detector() {
        return "detector";
    }

}
