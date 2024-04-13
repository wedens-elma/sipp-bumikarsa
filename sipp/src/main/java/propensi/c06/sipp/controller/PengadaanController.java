package propensi.c06.sipp.controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import propensi.c06.sipp.dto.PengadaanMapper;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.BarangRencana;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.PengadaanDb;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.PengadaanService;
import propensi.c06.sipp.service.RencanaService;
import propensi.c06.sipp.service.UserService;
import propensi.c06.sipp.service.VendorService;

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

    private RencanaService rencanaService;

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

//    @GetMapping("/pengadaan/{id}/updateStatus")
//    public String updateStatus(@PathVariable("id") String id, Model model) {
//        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
//        model.addAttribute("pengadaan", pengadaan);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        return "formUpdateStatus";
//
//    }

//    @GetMapping("/pengadaan/{id}/updateStatus")
//    public String updateStatus(@PathVariable("id") String id, Model model) {
//        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
//        model.addAttribute("dto", pengadaan);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        return "formUpdateStatus";
//
//    }

//    @PostMapping("/pengadaan/{id}/updateStatus")
//    public String submitStatus(@PathVariable("id") String id, @ModelAttribute("pengadaan") Pengadaan pengadaan, @RequestParam("shipmentStatus") int shipmentStatus,
//                               @RequestParam("paymentStatus") int paymentStatus){
//        pengadaanService.updateStatusShipment(id, shipmentStatus, paymentStatus);
//        return "viewAllPengadaan";
//    }
//    @PostMapping("/pengadaan/updateStatus")
//    public String submitStatus(@Valid @ModelAttribute PengadaanRequestDTO dto, Model model){
//        Pengadaan pengadaan = pengadaanService.updateStatusShipment(dto);
//        return "redirect:/pengadaan/" + pengadaan.getIdPengadaan();
//    }


//    @GetMapping("/pengadaan/{id}/update")
//    public String formUpdatePengadaan(@PathVariable String id, Model model) {
//        // Dapatkan detail pengadaan yang akan diperbarui
//        Pengadaan pengadaanToUpdate = pengadaanService.getPengadaanDetail(id);
//
//        // Pastikan pengadaan dapat diperbarui berdasarkan status pengiriman dan pembayaran
//        if (pengadaanToUpdate.getShipmentStatus() != 0 || pengadaanToUpdate.getPaymentStatus() != 0) {
//            // Redirect ke halaman gagal jika pengadaan tidak dapat diperbarui
//            return "successAddPengadaan";
//        }
//        pengadaanToUpdate.setShipmentStatus(0);
//        pengadaanToUpdate.setPaymentStatus(0);
//        // Konversi pengadaan menjadi DTO untuk ditampilkan di form
//        UpdatePengadaanRequestDTO dto = pengadaanMapper.pengadaanToUpdatePengadaanRequestDTO(pengadaanToUpdate);
//
//        // Siapkan model untuk ditampilkan di form
//        model.addAttribute("dto", dto);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//
//        // Tampilkan halaman form update pengadaan
//        return "formUpdatePengadaan";
//    }
//
//    @PostMapping("/pengadaan/{id}/update")
//    public String updatePengadaan(@Valid @ModelAttribute UpdatePengadaanRequestDTO dto, Model model) {
//        String id = dto.getIdPengadaan();
//        // Pastikan pengadaan dapat diperbarui berdasarkan status pengiriman dan pembayaran
//        Pengadaan pengadaanToUpdate = pengadaanService.getPengadaanDetail(id);
////        if (pengadaanToUpdate.getShipmentStatus() != 0 || pengadaanToUpdate.getPaymentStatus() != 0) {
////            // Redirect ke halaman gagal jika pengadaan tidak dapat diperbarui
////            return "successAddPengadaan";
////        }
//        if (pengadaanToUpdate == null) {
//            // Tindakan yang sesuai jika pengadaan tidak ditemukan (misalnya, tampilkan pesan kesalahan)
//            return "successAddPengadaan";
//        }
//        pengadaanToUpdate.setShipmentStatus(0);
//        pengadaanToUpdate.setPaymentStatus(0);
//
//        // Update pengadaan
//        pengadaanService.updatePengadaan(dto);
//
//        // Redirect ke halaman sukses jika pengadaan berhasil diperbarui
//        model.addAttribute("idPengadaan", dto.getIdPengadaan());
//        return "redirect:/pengadaan";
//    }

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
//
//    @GetMapping("/pengadaan/{id}/update")
//    public String formUpdatePengadaan(@PathVariable String id, Model model) {
//        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
//
//        // Check apakah pengadaan dapat diupdate (shipment status == 0 dan payment status == 0)
//        if (pengadaan.getShipmentStatus() == 0 && pengadaan.getPaymentStatus() == 0) {
//            model.addAttribute("pengadaan", pengadaan);
//            model.addAttribute("listVendor", vendorService.getAllVendors());
//            model.addAttribute("listBarang", barangService.getAllBarang());
//            return "formUpdatePengadaan";
//        } else {
//            // Jika tidak dapat diupdate, redirect kembali ke halaman detail pengadaan
//            return "redirect:/pengadaan/" + id;
//        }
//    }
//
//    @PostMapping("/pengadaan/{id}/update")
//    public String updatePengadaan(@PathVariable String id, @Valid @ModelAttribute PengadaanRequestDTO dto, Model model) {
//        dto.setIdPengadaan(id);
//        dto.setShipmentStatus(0);
//        dto.setPaymentStatus(0);
//        pengadaanService.addPengadaan(dto);
//        return "redirect:/pengadaan/" + id;
//    }

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


//    @GetMapping(value = "/pengadaan/{id}/update")
//    public String formUpdate(@PathVariable(value = "id") String id, Model model) {
//        var pengadaan = pengadaanService.getPengadaanDetail(id);
//        var pengadaanDTO = pengadaanMapper.pengadaanToUpdatePengadaanRequestDTO(pengadaan);
//
//        model.addAttribute("pengadaanDTO", pengadaanDTO);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listbarang", barangService.getAllBarang());
//        return "form-update-pengadaan";
//    }

    @PostMapping(value = "/pengadaan/{id}/update", params = {"addRow"})
    public String addRowUpdateBarang(@ModelAttribute PengadaanRequestDTO dto, Model model, @RequestParam("addRow") String addRow) {
        if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
            dto.setListBarang(new ArrayList<>());
        }
        dto.getListBarang().add(new PengadaanBarang());
        model.addAttribute("pengadaanDTO", dto);
        model.addAttribute("listVendor", vendorService.getAllVendors());
        model.addAttribute("listBarang", barangService.getAllBarang());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "updateForm";
    }


    @PostMapping(value = "/pengadaan/{id}/update", params = {"deleteRow"})
    public String deleteRowUpdateBarang(Model model, @ModelAttribute PengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
        dto.getListBarang().remove(row);
        model.addAttribute("pengadaanDTO", dto);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("listVendor", vendorService.getAllVendors());


        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        return "updateForm";

    }

    @PostMapping("pengadaan/update")
    public String updatePengadaan(@Valid @ModelAttribute UpdatePengadaanRequestDTO pengadaanDTO, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            var errorMessage = "Data yang anda kirimkan tidak valid";
            model.addAttribute("errorMessage", errorMessage);
            return "error-view";
        }

        var pengadaanFromDto = pengadaanMapper.updatePengadaanRequestDTOToPengadaan(pengadaanDTO);
        var pengadaan = pengadaanService.updatePengadaan(pengadaanFromDto);
        model.addAttribute("idPengadaan", pengadaan.getIdPengadaan());
        System.out.println("ini idnyaa"+pengadaan.getIdPengadaan());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);

        return "success-update-pengadaan";
    }

//    @PostMapping(value = "/pengadaan/{id}/update")
//    public String handleUpdateAction(@PathVariable(value = "id") String id, @RequestParam("action") String action, @ModelAttribute PengadaanRequestDTO dto, @RequestParam(value = "row", required = false) Integer row, Model model) {
//        if ("addRow".equals(action)) {
//            // Panggil metode untuk menambahkan baris
//            if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
//                dto.setListBarang(new ArrayList<>());
//            }
//            dto.getListBarang().add(new PengadaanBarang());
//        } else if ("deleteRow".equals(action)) {
//            // Panggil metode untuk menghapus baris
//            if (row != null && row >= 0 && row < dto.getListBarang().size()) {
//                dto.getListBarang().remove(row);
//            }
//        }
//        model.addAttribute("pengadaanDTO", dto);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        String username = userService.getCurrentUserName();
//        model.addAttribute("username", username);
//        return "updateForm";
//    }
//
//
//    @PostMapping("pengadaan/update")
//    public String updatePengadaan(@Valid @ModelAttribute UpdatePengadaanRequestDTO pengadaanDTO, BindingResult bindingResult,
//                                  Model model) {
//
//        if (bindingResult.hasErrors()) {
//            var errorMessage = "Data yang anda kirimkan tidak valid";
//            model.addAttribute("errorMessage", errorMessage);
//            return "error-view";
//        }
//
//        var pengadaanFromDto = pengadaanMapper.updatePengadaanRequestDTOToPengadaan(pengadaanDTO);
//        var pengadaan = pengadaanService.updatePengadaan(pengadaanFromDto);
//        model.addAttribute("idPengadaan", pengadaan.getIdPengadaan());
//        System.out.println("ini idnyaa" + pengadaan.getIdPengadaan());
//        String username = userService.getCurrentUserName();
//        model.addAttribute("username", username);
//
//        return "success-update-pengadaan";
//    }


//    @GetMapping("/pengadaan/update/{id}")
//    public String updatePengadaan(Model model, @PathVariable String id){
//        try {
//            Pengadaan pengadaan = pengadaanDb.findById(id).get();
//            model.addAttribute("pengadaan", pengadaan);
//
//            PengadaanRequestDTO dto = new PengadaanRequestDTO();
//            dto.setNamaPengadaan(pengadaan.getNamaPengadaan());
//            dto.setTanggalPengadaan (pengadaan.getTanggalPengadaan().toString());
//            dto.setVendor(pengadaan.getVendor());
//            dto.setDiskonKeseluruhan(pengadaan.getDiskonKeseluruhan());
//            //dto.setListBarang(pengadaan.getListPengadaanBarang());
//            dto.setPaymentStatus(pengadaan.getPaymentStatus());
//            dto.setShipmentStatus(pengadaan.getShipmentStatus());
//
//            model.addAttribute("dto", dto);
//        }
//        catch (Exception ex){
//            System.out.println("Exception: " + ex.getMessage());
//            return "redirect:/pengadaan";
//        }
//
//        return "formUpdatePengadaan";
//    }




    @GetMapping("/pengadaan/tambah")
    public String formAddPengadaan(@RequestParam(required=false) Long idRencana, Model model) {
        PengadaanRequestDTO dtoPengadaan = new PengadaanRequestDTO();

        if (idRencana != null) {
            Rencana rencana = rencanaService.getRencanaById(idRencana);
            dtoPengadaan.setNamaPengadaan(rencana.getNamaRencana());
            dtoPengadaan.setVendor(rencana.getVendor());
            dtoPengadaan.setListBarang(new ArrayList<>());
            for (BarangRencana barangRencana : rencana.getListBarangRencana()) {
                PengadaanBarang barangPengadaan = new PengadaanBarang();
                barangPengadaan.setBarang(barangRencana.getBarang());
                barangPengadaan.setJumlahBarang(barangRencana.getKuantitas());
                dtoPengadaan.getListBarang().add(barangPengadaan);
            }
            rencanaService.ubahStatusRencana(rencana, "direalisasikan", rencana.getLogRencana().get(rencana.getLogRencana().size()-1).getFeedback());
        }

        model.addAttribute("dto", dtoPengadaan);
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

//    @GetMapping("/pengadaan/{id}/update")
//    public String formUpdatePengadaan(@PathVariable String id, Model model){
//        Pengadaan pengadaan = pengadaanService.getPengadaanDetail(id);
//        List<PengadaanBarang> daftarBarang = pengadaan.getListPengadaanBarang();
//        model.addAttribute("daftarBarang", daftarBarang);
//        model.addAttribute("pengadaan", pengadaan);
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        return "formUpdatePengadaan";
//    }
//
//    @PostMapping(value = "/pengadaan/update", params = {"addRow"})
//    public String addRowUpdatePengadaan(@ModelAttribute PengadaanRequestDTO dto, Model model) {
//        if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
//            dto.setListBarang(new ArrayList<>());
//        }
//        dto.getListBarang().add(new PengadaanBarang());
//        model.addAttribute("dto", dto);
//
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        return "formUpdatePengadaan";
//    }
//
//
//    @PostMapping(value = "/pengadaan/update", params = {"deleteRow"})
//    public String deleteRowUpdatePengadaan(Model model, @ModelAttribute PengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
//        dto.getListBarang().remove(row);
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//
//        return "formUpdatePengadaan";
//
//    }
//
//    @PostMapping("/pengadaan/update")
//    public String updatePengadaan(@Valid @ModelAttribute PengadaanRequestDTO dto, Model model){
//        Pengadaan pengadaan = pengadaanService.updatePengadaan(dto);
//        model.addAttribute("pengadaan", pengadaan);
//        return "successUpdatePengadaan";
//    }


//    @GetMapping("/pengadaan/{id}/update")
//    public String formUpdate(@PathVariable("id") String id, Model model){
//        var pengadaan = pengadaanService.getPengadaanDetail(id);
//        var dto = pengadaanMapper.updatePengadaanRequestDTOToPengadaan(pengadaan);
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        return "formUpdatePengadaan";
//
//    }
//
//    @PostMapping(value="/pengadaan/update", params={"addRow"})
//    public String addRowBarang(@ModelAttribute UpdatePengadaanRequestDTO dto, Model model){
//        if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
//            dto.setListBarang(new ArrayList<>());
//        }
//
//        dto.getListBarang().add(new PengadaanBarang());
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        return "formUpdatePengadaan";
//    }
//
//    @PostMapping(value="/pengadaan/update", params={"deleteRoe"})
//    public String deleteRowBarang(@ModelAttribute UpdatePengadaanRequestDTO dto, @RequestParam("deleteRow") int row, Model model){
//        dto.getListBarang().remove(row);
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        return "formUpdatePengadaan";
//    }
//
//    @PostMapping("/pengadaan/update")
//    public String updatePengadaan(@Valid @ModelAttribute UpdatePengadaanRequestDTO dto, BindingResult bindingResult, Model model){
//        var pengadaandto = pengadaanMapper.updatePengadaanRequestDTOToPengadaan(dto);
//        var pengadaan = pengadaanService.updatePengadaan(pengadaandto);
//
//    }


//    @GetMapping("/pengadaan/{id}/update")
//    public String formUpdate(@PathVariable("id") String id, Model model){
//        var pengadaan = pengadaanService.getPengadaanDetail(id);
//        var dto = pengadaanMapper.pengadaanToUpdatePengadaanRequestDTO(pengadaan);
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        return "formUpdatePengadaan";
//
//    }
//
//    @PostMapping(value = "/pengadaan/update", params = {"addRow"})
//    public String addRowTambah(@ModelAttribute UpdatePengadaanRequestDTO dto, Model model) {
//        if (dto.getListBarang() == null || dto.getListBarang().size() == 0) {
//            dto.setListBarang(new ArrayList<>());
//        }
//        dto.getListBarang().add(new PengadaanBarang());
//        model.addAttribute("dto", dto);
//
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        return "formUpdatePengadaan";
//    }
//
//    @PostMapping(value = "/pengadaan/update", params = {"deleteRow"})
//    public String deleteRowTambah(Model model, @ModelAttribute UpdatePengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
//        dto.getListBarang().remove(row);
//        model.addAttribute("dto", dto);
//        model.addAttribute("listBarang", barangService.getAllBarang());
//        model.addAttribute("listVendor", vendorService.getAllVendors());
//
//        return "formUpdatePengadaan";
//
//    }
//
//    @PostMapping("/pengadaan/update")
//    public String updatePengadaan(@Valid @ModelAttribute Pengadaan dto, Model model){
//
//        var pengadaan = pengadaanMapper.pengadaanToUpdatePengadaanRequestDTO(dto);
//        var pengada= pengadaanService.updatePengadaan(pengadaan);
//
//
//        pengada.setPaymentStatus(0);
//        pengada.setShipmentStatus(0);
//
//
//        String id = pengada.getIdPengadaan();
//
//       // Pengadaan pengadaan = pengadaanService.addPengadaan(dto);
//        model.addAttribute("idPengadaan", id);
//
//        return "successAddPengadaan";
//    }









}
