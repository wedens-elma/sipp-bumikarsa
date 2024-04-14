package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
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
    public String home(Model model) {
        model.addAttribute("countPaymentBelumDibayar", pengadaanService.countPaymentStatus("belum dibayar"));
        model.addAttribute("countPaymentSudahDibayar", pengadaanService.countPaymentStatus("sudah dibayar"));
        model.addAttribute("countPaymentSedangDiproses", pengadaanService.countPaymentStatus("sedang diproses"));
        model.addAttribute("countShipmentSedangDiproses", pengadaanService.countShipmentStatus("sedang diproses"));
        model.addAttribute("countShipmentSudahSampai", pengadaanService.countShipmentStatus("sudah sampai"));
        model.addAttribute("countAllPengadaan", pengadaanService.countAllPengadaan());
        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "index-admin.html";
        } else {
            return "index.html";
        }
    }
}
