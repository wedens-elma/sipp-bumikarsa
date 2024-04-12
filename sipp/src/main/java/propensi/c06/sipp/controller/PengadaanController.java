package propensi.c06.sipp.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.BarangRencana;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.PengadaanService;
import propensi.c06.sipp.service.RencanaService;
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
    private RencanaService rencanaService;

    @GetMapping("/pengadaan")
    public String listPengadaan(Model model){
        //List<Pengadaan> listPengadaan = pengadaanService.getAllPengadaan();
        List<Pengadaan> listPengadaan = pengadaanService.getAllPengadaan();
        model.addAttribute("listPengadaan", listPengadaan);
        return "viewAllPengadaan";
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
        return "detailPengadaan";
    }


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
        return "formAddPengadaan";
    }


    @PostMapping(value = "/pengadaan/tambah", params = {"deleteRow"})
    public String deleteRowTambahBarang(Model model, @ModelAttribute PengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
        dto.getListBarang().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("listVendor", vendorService.getAllVendors());

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
        return "successAddPengadaan";
    }






}
