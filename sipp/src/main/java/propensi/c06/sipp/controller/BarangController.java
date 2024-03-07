package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import propensi.c06.sipp.dto.BarangMapper;
import propensi.c06.sipp.dto.request.CreateTambahBarangRequestDTO;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.service.BarangService;

@RestController
public class BarangController {
    @Autowired
    BarangDb barangDb;

    @Autowired
    BarangMapper barangMapper;

    @Autowired
    private BarangService barangService;

    @GetMapping("/barang/tambah")
    public String formTambahBarang(Model model){

        var barangDTO = new CreateTambahBarangRequestDTO();

        model.addAttribute("barangDTO", barangDTO);

        return "form-tambah-barang.html";
    }

    @PostMapping("/barang/tambah")
    public String addTambahBarang(CreateTambahBarangRequestDTO barangDTO, Model model){
        System.out.println("MASUK SINI 2");
        var barang = barangMapper.createTambahBarangRequestDTO(barangDTO);
        System.out.println(barang.toString());

        barangService.addBarang(barang);

        model.addAttribute("kode", barang.getKodeBarang());
        
        return "success-tambah-barang.html";
    }

}
