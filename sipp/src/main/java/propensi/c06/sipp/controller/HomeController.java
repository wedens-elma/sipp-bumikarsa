package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import propensi.c06.sipp.service.UserService;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/") 
    public String home() {

        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "index-admin.html";
        }
        return "index.html";
    }
}
