package propensi.c06.sipp.controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import propensi.c06.sipp.dto.PengadaanMapper;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.CreateTambahBarangRequestDTO;
import propensi.c06.sipp.dto.request.PengadaanBarangDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.PengadaanDb;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.PengadaanService;
import propensi.c06.sipp.service.UserService;
import propensi.c06.sipp.service.VendorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

@Controller
public class PengadaanController {

    @Autowired
    private PengadaanService pengadaanService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private PengadaanMapper pengadaanMapper;

    @Autowired
    private PengadaanDb pengadaanDb;

    @Autowired
    private UserService userService;

    @GetMapping("/pengadaan")
    public String listPengadaan(Model model){
        //List<Pengadaan> listPengadaan = pengadaanService.getAllPengadaan();
        List<Pengadaan> listPengadaan = pengadaanService.getAllPengadaan();
        model.addAttribute("listPengadaan", listPengadaan);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        if(userService.getCurrentUserRole().equalsIgnoreCase("manajer") || userService.getCurrentUserRole().equalsIgnoreCase("keuangan")){
            return "viewAllPengadaanKeuanganManajer.html";
        } else{
            return "viewAllPengadaan.html";
        }
    }

    @GetMapping("/pengadaan/{id}")
    public String detailPengadaan(@PathVariable String id, Model model){
        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
        Map<String, Float> totalHargaMap = pengadaanService.hitungTotalHarga(pengadaan);

        float totalHargaAwal = totalHargaMap.get("totalHargaAwal");
        float totalHargaDiskonSatuan = totalHargaMap.get("totalHargaDiskonSatuan");
        float totalHargaAkhir = totalHargaMap.get("totalHargaAkhir");
        model.addAttribute("pengadaan", pengadaan);
        model.addAttribute("totalHargaAwal", totalHargaAwal);
        model.addAttribute("totalHargaDiskonSatuan", totalHargaDiskonSatuan);
        model.addAttribute("totalHargaAkhir", totalHargaAkhir);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        if(userService.getCurrentUserRole().equalsIgnoreCase("manajer") || userService.getCurrentUserRole().equalsIgnoreCase("keuangan")){
            return "detailPengadaanKeuanganManajer.html";
        } else{
            return "detailPengadaan.html";
        }

    }

    @GetMapping("/pengadaan/{id}/updateShipmentStatus")
    public String formUpdateShipmentStatusPengadaan(@PathVariable String id, Model model){
        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
        model.addAttribute("pengadaan", pengadaan);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "formUpdateShipmentStatusPengadaan";
    }

    @PostMapping("/pengadaan/{id}/updateShipmentStatus")
    public String updateStatusShipmentPengadaan(@PathVariable String id, @RequestParam("shipmentStatus") int shipmentStatus){
        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
        pengadaan.setShipmentStatus(shipmentStatus);
        //pengadaan.setPaymentStatus(paymentStatus);
        pengadaanService.updateStatusPengadaan(pengadaan); // Buat method ini di PengadaanService
        return "redirect:/pengadaan";
    }

    @GetMapping("/pengadaan/{id}/updatePaymentStatus")
    public String formUpdateStatusPengadaan(@PathVariable String id, Model model){
        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
        model.addAttribute("pengadaan", pengadaan);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "formUpdatePaymentStatusPengadaan";
    }

    @PostMapping("/pengadaan/{id}/updatePaymentStatus")
    public String updateStatusPengadaan(@PathVariable String id, @RequestParam("paymentStatus") int paymentStatus){
        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
        //pengadaan.setShipmentStatus(shipmentStatus);
        pengadaan.setPaymentStatus(paymentStatus);
        pengadaanService.updateStatusPengadaan(pengadaan); // Buat method ini di PengadaanService
        return "redirect:/pengadaan";
    }

    ///semifinall
    @GetMapping(value = "/pengadaan/{id}/update")
    public String formUpdate(@PathVariable(value = "id") String id, Model model) {
        var pengadaan = pengadaanService.getPengadaanDetail(id);
        // Periksa apakah pengadaan memenuhi syarat untuk diperbarui
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        if (pengadaan.getShipmentStatus() == 0 && pengadaan.getPaymentStatus() == 0) {
            var pengadaanDTO = pengadaanMapper.pengadaanToUpdatePengadaanRequestDTO(pengadaan);
            pengadaanDTO.setIdPengadaan(id);
            model.addAttribute("pengadaanDTO", pengadaanDTO);
            model.addAttribute("listVendor", vendorService.getAllVendors());
            model.addAttribute("listbarang", barangService.getAllBarang());
            return "updateForm";
        } else {
            // Tampilkan pesan bahwa pengadaan tidak dapat diperbarui
            model.addAttribute("errorMessage", "Pengadaan tidak memenuhi syarat untuk diperbarui.");
            return "error-view"; // Ganti dengan halaman atau tindakan yang sesuai
        }
    }

    ///hampirrrrrr//
    @PostMapping("pengadaan/{id}/update")
    public String updatePengadaan(@Valid @ModelAttribute UpdatePengadaanRequestDTO dto, BindingResult bindingResult,
                                  Model model, HttpServletRequest request) {

        if(request.getParameter("tambahRow") != null){
            if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
                dto.setListBarang(new ArrayList<>());
            }
            dto.getListBarang().add(new PengadaanBarang());
            model.addAttribute("pengadaanDTO", dto);
            model.addAttribute("listBarang", barangService.getAllBarang());
            model.addAttribute("listVendor", vendorService.getAllVendors());
            // ... add other attributes if needed ...
            return "updateForm";
        } else if(request.getParameter("hapusRow") != null){
            int rowIndex = Integer.parseInt(request.getParameter("hapusRow"));
            // ... remove row logic ...
            dto.getListBarang().remove(rowIndex);
            model.addAttribute("listBarang", barangService.getAllBarang());
            model.addAttribute("listVendor", vendorService.getAllVendors());
            model.addAttribute("pengadaanDTO", dto);
            // ... add other attributes if needed ...
            return "updateForm";

        } else{
            var pengadaanFromDto = pengadaanMapper.updatePengadaanRequestDTOToPengadaan(dto);
            var pengadaan = pengadaanService.updatePengadaan(pengadaanFromDto);
            model.addAttribute("idPengadaan", pengadaan.getIdPengadaan());
            System.out.println("ini idnyaa"+pengadaan.getIdPengadaan());
            String username = userService.getCurrentUserName();
            model.addAttribute("username", username);

            return "success-update-pengadaan";
        }
    }
/// di atas semi finaallll




    @GetMapping("/pengadaan/tambah")
    public String formAddPengadaan(Model model) {
        model.addAttribute("dto", new PengadaanRequestDTO());
        model.addAttribute("listVendor", vendorService.getAllVendors());
        model.addAttribute("listBarang", barangService.getAllBarang());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "formAddPengadaan";
    }



    @PostMapping(value = "/pengadaan/tambah", params = {"addRow"})
    public String addRowTambahBarang(@ModelAttribute PengadaanRequestDTO dto, Model model) {
        if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
            dto.setListBarang(new ArrayList<>());
        }
        dto.getListBarang().add(new PengadaanBarang());
        model.addAttribute("dto", dto);

        model.addAttribute("listVendor", vendorService.getAllVendors());
        model.addAttribute("listBarang", barangService.getAllBarang());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "formAddPengadaan";
    }


    @PostMapping(value = "/pengadaan/tambah", params = {"deleteRow"})
    public String deleteRowTambahBarang(Model model, @ModelAttribute PengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
        dto.getListBarang().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("listVendor", vendorService.getAllVendors());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "formAddPengadaan";

    }

    @PostMapping("/pengadaan/tambah")
    public String addPengadaan(@Valid @ModelAttribute PengadaanRequestDTO dto, Model model){

//        Map<String, Integer> totalHargaMap = pengadaanService.hitungTotalHarga(dto);
//
//        int totalHargaAwal = totalHargaMap.get("totalHargaAwal");
//        int totalHargaSetelahDiskon = totalHargaMap.get("totalHargaSetelahDiskon");

        dto.setPaymentStatus(0);
        dto.setShipmentStatus(0);
        String id = dto.getIdPengadaan();

        Pengadaan pengadaan = pengadaanService.addPengadaan(dto);
        model.addAttribute("idPengadaan", id);
//        model.addAttribute("totalHargaAwal", totalHargaAwal);
//        model.addAttribute("totalHargaSetelahDiskon", totalHargaSetelahDiskon);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "successAddPengadaan";
    }


    @GetMapping("/pengadaan/{id}/delete")
    public String deletePengadaanBarang(@PathVariable("id") String id, Model model){
        pengadaanService.deletePengadaan(id);
        model.addAttribute("id", id);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        return "successDeletePengadaan";
    }

}
