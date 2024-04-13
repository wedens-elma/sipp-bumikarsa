package propensi.c06.sipp.restcontroller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.service.PengadaanService;

@RestController
@RequestMapping("/api/pengadaan")
public class PengadaanRestController {

    @Autowired
    private PengadaanService pengadaanService;


    @GetMapping("/view-all")
    public ResponseEntity<Map<String, Double>> getMonthlyExpenditure() {
        Map<String, Double> data = pengadaanService.getTotalPengeluaranPerbulan();
        if (data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }


    // pertahun
    @GetMapping("/view-all-annual")
    public ResponseEntity<Map<String, Double>> getAnnualExpenditure() {
        Map<String, Double> data = pengadaanService.getTotalPengeluaranPertahun();
        if (data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/total-pengadaan")
    public ResponseEntity<Integer> getTotalPengadaans() {
        int totalPengadaans = pengadaanService.getTotalNumberOfPengadaans();
        return ResponseEntity.ok(totalPengadaans);  // Returns the total count of procurements
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboardData() {
        Map<String, Double> monthly = pengadaanService.getTotalPengeluaranPerbulan();
        Map<String, Double> annual = pengadaanService.getTotalPengeluaranPertahun();
        int totalPengadaan = pengadaanService.getTotalNumberOfPengadaans();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("pengeluaranBulanan", monthly);
        dashboard.put("pengeluaranTahunan", annual);
        dashboard.put("totalPengadaan", totalPengadaan);

        return ResponseEntity.ok(dashboard);
    }

}
