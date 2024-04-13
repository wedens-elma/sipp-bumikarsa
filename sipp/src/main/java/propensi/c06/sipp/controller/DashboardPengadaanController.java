package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import propensi.c06.sipp.service.PengadaanService;

import java.util.Map;

@Controller
public class DashboardPengadaanController {

    @Autowired
    private PengadaanService pengadaanService;

//    @GetMapping("/dashboard")
//    public String showDashboard(Model model) {
//        DashboardDTO dashboard = new DashboardDTO();
//        dashboard.setPengeluaranBulanan(pengadaanService.getTotalPengeluaranPerbulan());
//        dashboard.setPengeluaranTahunan(pengadaanService.getTotalPengeluaranPertahun());
//        dashboard.setTotalPengadaan(pengadaanService.getTotalNumberOfPengadaans());
//
//        model.addAttribute("dashboard", dashboard);
//        return "view-dashboard-pengadaan"; // Nama file HTML yang akan menampilkan data dashboard
//    }

    @GetMapping("pengadaan/dashboard-pengadaan")
    public String showDashboard(Model model) {
        Map<String, Double> pengeluaranBulanan = pengadaanService.getTotalPengeluaranPerbulan();
        model.addAttribute("pengeluaranBulanan", pengeluaranBulanan);

        Map<String, Double> totalPengeluaranPertahun = pengadaanService.getTotalPengeluaranPertahun();
        model.addAttribute("totalPengeluaranTahunan", totalPengeluaranPertahun);
        return "view-dashboard-pengadaan";
    }

}
