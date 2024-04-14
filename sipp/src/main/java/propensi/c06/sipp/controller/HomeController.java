package propensi.c06.sipp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.service.PengadaanService;
import propensi.c06.sipp.service.UserService;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    PengadaanService pengadaanService;

    @GetMapping("/") 
    public String home(Principal principal, Model model) {

        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);

        List<Pengadaan> top5LatestPengadaan = pengadaanService.getTop5LatestPengadaan();
        System.out.println(top5LatestPengadaan);
        model.addAttribute("top5LatestPengadaan", top5LatestPengadaan);

        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "index-admin.html";
        }
        return "index.html";
    }
}
