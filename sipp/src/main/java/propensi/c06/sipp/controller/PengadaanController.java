package propensi.c06.sipp.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.PengadaanService;
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
        model.addAttribute("pengadaan", pengadaan);
        return "detailPengadaan";
    }


    @GetMapping("/pengadaan/tambah")
    public String formAddPengadaan(Model model) {
        model.addAttribute("dto", new PengadaanRequestDTO());
        model.addAttribute("listVendor", vendorService.getAllVendor());
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

        model.addAttribute("listVendor", vendorService.getAllVendor());
        model.addAttribute("listBarang", barangService.getAllBarang());
        return "formAddPengadaan";
    }


    @PostMapping(value = "/pengadaan/tambah", params = {"deleteRow"})
    public String deleteRowTambahBarang(Model model, @ModelAttribute PengadaanRequestDTO dto, @RequestParam("deleteRow") int row){
        dto.getListBarang().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("listVendor", vendorService.getAllVendor());

        return "formAddPengadaan";

    }

    @PostMapping("/pengadaan/tambah")
    public String addPengadaan(@Valid @ModelAttribute PengadaanRequestDTO dto, Model model){

        int totalHargaAwal = 0;
        int totalHargaAKhirDiskonSatuan = 0;
        for (PengadaanBarang x: dto.getListBarang()){
            totalHargaAwal+=x.getHargaBarang();
            int hargaAKhirDiskonSatuan = x.getHargaBarang() - (x.getDiskonSatuan()*x.getHargaBarang());
            totalHargaAKhirDiskonSatuan+=hargaAKhirDiskonSatuan;

        }
        //int totalHargaSetelahDiskon=totalHargaAwal -(totalHargaAwal*(dto. getPengadaan().getDiskonKeseluruhan())) ;
        dto.setPaymentStatus(0);
        dto.setShipmentStatus(0);
        Pengadaan pengadaan = pengadaanService.addPengadaan(dto);
        //model.addAttribute("kodePengadaan", kode);
        model.addAttribute("totalHargaAwal", totalHargaAwal);
        //model.addAttribute("totalHargaAkhir", totalHargaSetelahDiskon);
        return "successAddPengadaan";
    }





}
