package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class PageController {

    @Autowired
    ServerProperties serverProperties;

    @RequestMapping("port")
    public String ActivePort(Model model) {

        model.addAttribute("port", serverProperties.getPort());

        return "active-port";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    // @RequestMapping("/error")
    // public String handleError(HttpServletRequest request) {
    //     Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
    //     if (status != null) {
    //         Integer statusCode = Integer.valueOf(status.toString());
        
    //         if(statusCode == HttpStatus.BAD_REQUEST.value()) {
    //             return "400";
    //         }
    //         else if(statusCode == HttpStatus.NOT_FOUND.value()) {
    //             return "404";
    //         }
    //         else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
    //             return "500";
    //         }
    //     }
    //     return "error";
    // }
    
}