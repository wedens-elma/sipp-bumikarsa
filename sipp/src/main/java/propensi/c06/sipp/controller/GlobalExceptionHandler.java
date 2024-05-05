package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import propensi.c06.sipp.service.UserService;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    UserService userService;

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e, Model model) {
        int httpStatusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (httpStatusCode == HttpServletResponse.SC_FORBIDDEN) { // Check for 403 Forbidden
            model.addAttribute("errorMessage", "Anda tidak memiliki akses kepada halaman ini");
        }
        else if (httpStatusCode == HttpServletResponse.SC_NOT_FOUND) {
            model.addAttribute("errorMessage", "Halaman tidak ditemukan");
        }
        else if (httpStatusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            model.addAttribute("errorMessage", "Internal Server Error");
        }
        else {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }
        return "error";
    }
}
