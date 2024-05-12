package propensi.c06.sipp.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("username", user.getName());

        List<Pengadaan> top5LatestPengadaan = pengadaanService.getTop5LatestPengadaan();
        System.out.println(top5LatestPengadaan);
        model.addAttribute("top5LatestPengadaan", top5LatestPengadaan);

        addDashboardDataToModel(model);

        model.addAttribute("countPaymentBelumDibayar", pengadaanService.countPaymentStatus("belum dibayar"));
        model.addAttribute("countPaymentSudahDibayar", pengadaanService.countPaymentStatus("sudah dibayar"));
        model.addAttribute("countPaymentSedangDiproses", pengadaanService.countPaymentStatus("sedang diproses"));
        model.addAttribute("countShipmentSedangDiproses", pengadaanService.countShipmentStatus("sedang diproses"));
        model.addAttribute("countShipmentSudahSampai", pengadaanService.countShipmentStatus("sudah sampai"));
        model.addAttribute("countAllPengadaan", pengadaanService.countAllPengadaan());
        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            model.addAttribute("userRole", userService.getCurrentUserRole().toLowerCase());
            return "index-admin.html";
        } else {
            model.addAttribute("userRole", userService.getCurrentUserRole().toLowerCase());
            return "index.html";
        }
    }

    private void addDashboardDataToModel(Model model) {
        Map<String, Double> pengeluaranBulanan = pengadaanService.getTotalPengeluaranPerbulan();
        model.addAttribute("pengeluaranBulanan", pengeluaranBulanan);

        Map<String, Double> totalPengeluaranPertahun = pengadaanService.getTotalPengeluaranPertahun();
        double total = totalPengeluaranPertahun.values().stream().mapToDouble(Double::doubleValue).sum();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String totalFormatted = kursIndonesia.format(total);

        model.addAttribute("totalPengeluaranTahunan", totalFormatted);
        int totalPengadaan = pengadaanService.getTotalNumberOfPengadaans();
        model.addAttribute("totalPengadaan", totalPengadaan);
    }
}
