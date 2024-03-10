package propensi.c06.sipp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
    public String daftarBarang(Model model) {
        List<Barang> listBarang = barangService.getAllBarang();
        model.addAttribute("listBarang", listBarang);

        return "viewall-barang.html";
    }

    @GetMapping("/barang/tambah")
    public String formTambahBarang(Model model) {

        var barangDTO = new CreateTambahBarangRequestDTO();

        model.addAttribute("barangDTO", barangDTO);

        return "form-tambah-barang.html";
    }

    @PostMapping("/barang/tambah")
    public String addTambahBarang(CreateTambahBarangRequestDTO barangDTO, Model model,
            @RequestPart("file") MultipartFile file) {
        var barang = barangMapper.createTambahBarangRequestDTO(barangDTO);

        if (barangService.isBarangExists(barang.getNamaBarang())) {
            model.addAttribute("error", "Barang sudah terdaftar");
    
            // Redirect to a page showing the error message and then redirecting back to the form after 5 seconds
            return "failed-tambah-barang.html";
        }

        byte[] imageContent;

        try {
            imageContent = barangService.processFile(file);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the file");
        }

        barang.setImage(imageContent);

        barangService.addBarang(barang);

        

        model.addAttribute("kodeBarang", barang.getKodeBarang());

        return "success-tambah-barang.html";
    }

    @GetMapping("/barang/{idBarang}")
    public String detailBarang(@PathVariable(value = "idBarang") String kodeBarang, Model model) {
        var barang = barangService.getBarangById(kodeBarang);

        // Encode byte array image to Base64 string
        String base64Image = Base64.getEncoder().encodeToString(barang.getImage());

        model.addAttribute("barang", barang);
        model.addAttribute("base64Image", base64Image);

        return "view-detail-barang.html";
    }

}
