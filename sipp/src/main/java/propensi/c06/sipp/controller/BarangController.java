package propensi.c06.sipp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import propensi.c06.sipp.dto.BarangMapper;
import propensi.c06.sipp.dto.request.CreateTambahBarangRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.service.BarangService;



@Controller
public class BarangController {
    @Autowired
    BarangDb barangDb;

    @Autowired
    BarangMapper barangMapper;

    @Autowired
    private BarangService barangService;

    @GetMapping("/barang")
    public String daftarBarang(Model model){
        List<Barang> listBarang = barangService.getAllBarang();
        model.addAttribute("listBarang", listBarang);

        return "viewall-barang.html";
    }

    @GetMapping("/barang/tambah")
    public String formTambahBarang(Model model){

        var barangDTO = new CreateTambahBarangRequestDTO();

        model.addAttribute("barangDTO", barangDTO);

        return "form-tambah-barang.html";
    }

    @PostMapping("/barang/tambah")
    public String addTambahBarang(CreateTambahBarangRequestDTO barangDTO, Model model, @RequestParam("file") MultipartFile file){
        var barang = barangMapper.createTambahBarangRequestDTO(barangDTO);

        byte[] imageContent;
        try{
            imageContent = barangService.processFile(file);
        } catch (IOException e){
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the file"
            );
        }

        barang.setImage(imageContent);

        barangService.addBarang(barang);

        model.addAttribute("kodeBarang", barang.getKodeBarang());
        
        return "success-tambah-barang.html";
    }

    @GetMapping("/barang/{idBarang}")
    public String detailBarang(@PathVariable(value = "idBarang") String kodeBarang, Model model){
        var barang = barangService.getBarangById(kodeBarang);

        var barangResponseDTO = barangMapper.barangToReadBarangResponseDTO(barang);

        model.addAttribute("barang", barangResponseDTO);
        
        return "view-detail-barang.html";
    }

}
