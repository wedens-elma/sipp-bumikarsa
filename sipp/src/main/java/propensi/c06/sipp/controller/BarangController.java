package propensi.c06.sipp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import propensi.c06.sipp.dto.BarangMapper;
import propensi.c06.sipp.dto.request.CreateTambahBarangRequestDTO;
import propensi.c06.sipp.dto.request.UpdateBarangRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.LogBarang;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.LogRencanaService;
import propensi.c06.sipp.service.UserService;

@Controller
public class BarangController {
    @Autowired
    BarangDb barangDb;

    @Autowired
    BarangMapper barangMapper;

    @Autowired
    private BarangService barangService;

    @Autowired
    private UserService userService;


    @GetMapping("/barang")
    public String daftarBarang(Model model) {

        List<LogBarang> listLogBarang = barangService.getAllLogBarang();
        model.addAttribute("listLogBarang", listLogBarang);


        UserModel user = userService.getLoggedInUser();
        model.addAttribute("username", user.getName());
        
        List<Barang> listBarang = barangService.getAllBarang();

        for (int i = 0; i < listBarang.size(); i++) {
            System.out.println(listBarang.get(i));
        }

        // Membuat daftar barang yang stokBarang-nya kurang dari standarStokBarang
        List<Barang> listBarangStokKurang = new ArrayList<>();
        for (Barang barang : listBarang) {
            if (barang.getStokBarang() < barang.getStandarStokBarang() && barang.getIsDeleted() == false) {
                listBarangStokKurang.add(barang);
            }
        }

        // Mengecek apakah ada barang dengan stok kurang dari standar
        boolean adaBarangDenganStokKurangDariStandar = listBarang.stream()
                .anyMatch(barang -> (barang.getStokBarang() < barang.getStandarStokBarang()));

        String role = userService.getCurrentUserRole();

        

        model.addAttribute("listBarang", listBarang);

        model.addAttribute("listBarangStokKurang", listBarangStokKurang);

        model.addAttribute("adaBarangDenganStokKurangDariStandar", adaBarangDenganStokKurangDariStandar);

        model.addAttribute("role", role);

        return "viewall-barang.html";
    }

    @GetMapping("/barang/tambah")
    public String formTambahBarang(Model model) {
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("username", user.getName());

        var barangDTO = new CreateTambahBarangRequestDTO();

        model.addAttribute("barangDTO", barangDTO);

        return "form-tambah-barang.html";
    }

    @PostMapping("/barang/tambah")
    public String addTambahBarang(CreateTambahBarangRequestDTO barangDTO, Model model,
            @RequestPart("file") MultipartFile file) {
        var barang = barangMapper.createTambahBarangRequestDTO(barangDTO);

        if (barangService.isBarangExistsAndNotDeleted(barang.getNamaBarang())) {
            model.addAttribute("error", "Barang sudah terdaftar");

            // Redirect to a page showing the error message and then redirecting back to the
            // form after 5 seconds
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

        model.addAttribute("listLogBarang", barangService.getAllLogBarang());

        model.addAttribute("kodeBarang", barang.getKodeBarang());
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());

        return "success-tambah-barang.html";
    }

    @GetMapping("/barang/{idBarang}")
    public String detailBarang(@PathVariable(value = "idBarang") String kodeBarang, Model model) {
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("username", user.getName());

        var barang = barangService.getBarangById(kodeBarang);

        if (barang.getIsDeleted() == true){
            var errorMessage = "Barang yang anda cari tidak terdaftar dalam sistem.";

            model.addAttribute("errorMessage", errorMessage);

            return "error-view";
        }

        // Encode byte array image to Base64 string
        String base64Image = Base64.getEncoder().encodeToString(barang.getImage());

        String role = userService.getCurrentUserRole();
        model.addAttribute("role", role);

        model.addAttribute("barang", barang);
        model.addAttribute("base64Image", base64Image);

        return "view-detail-barang.html";
    }

    @GetMapping(value = "/barang/{kodeBarang}/update")
    public String formUpdateBuku(@PathVariable(value = "kodeBarang") String kodeBarang, Model model) {
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("username", user.getName());


        // Mendapatkan buku dengan id tersebut

        var barang = barangService.getBarangById(kodeBarang);

        // Memindahkan data buku ke DTO untuk selanjutnya diubah di form pengguna

        var barangDTO = barangMapper.barangToUpdateBarangRequestDTO(barang);

        model.addAttribute("barangDTO", barangDTO);

        return "form-update-barang";
    }

    @PostMapping("barang/update")
    public String updateBuku(@Valid @ModelAttribute UpdateBarangRequestDTO barangDTO, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            // Validasi gagal, kembalikan error
            var errorMessage = "Data yang anda kirimkan tidak valid";

            model.addAttribute("errorMessage", errorMessage);

            return "error-view";
        }


        if (barangDTO.getStokBarang() < 0 || barangDTO.getStandarStokBarang() < 0){
            model.addAttribute("kode", barangDTO.getKodeBarang());
            return "failed-update-barang";
        }


        var barangFromDto = barangMapper.updateBarangRequestDTOToBarang(barangDTO);
        var barang = barangService.updateBarang(barangFromDto);


        model.addAttribute("kodeBarang", barang.getKodeBarang());
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());        

        return "success-update-barang";
    }

    @GetMapping("/barang/{kodeBarang}/delete")
    public String softDeleteBuku(@PathVariable("kodeBarang") String kodeBarang, Model model) {
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("username", user.getName());

        barangService.softDeleteBarang(kodeBarang);
        model.addAttribute("kodeBarang", kodeBarang);
        return "success-delete-barang";
    }
}
