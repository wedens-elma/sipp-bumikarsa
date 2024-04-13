package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import propensi.c06.sipp.service.UserService;
import propensi.c06.sipp.service.PengadaanService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    private PengadaanService pengadaanService;

    @GetMapping("/")
    public String home(Model model) {
        // Assuming admin has a different index page logic
        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            addDashboardDataToModel(model);
            return "index-admin.html"; // Assuming admin might have a different page
        }
        return "index.html";
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
