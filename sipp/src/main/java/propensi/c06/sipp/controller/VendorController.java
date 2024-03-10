package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.service.VendorService;

import java.util.List;

@Controller
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/vendor")
    public String daftarVendor(Model model) {
        List<Vendor> listVendor = vendorService.getAllVendors();
        model.addAttribute("vendors", listVendor);
        System.out.print(listVendor);
        return "viewall-vendor";
    }

    @GetMapping("/vendor/tambah")
    public String formTambahVendor(Model model) {
        model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
        return "form-tambah-vendor";
    }

    @PostMapping("/vendor/tambah")
    public String addVendor(@ModelAttribute("vendorDTO") CreateVendorRequestDTO vendorDTO, RedirectAttributes redirectAttributes, Model model) {
//        try {
//            Vendor vendor = vendorService.addVendor(vendorDTO);
//            redirectAttributes.addFlashAttribute("successMessage", "Vendor berhasil ditambahkan dengan kode " + vendor.getKodeVendor() + ".");
//            return "redirect:/vendor";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan vendor. " + e.getMessage());
//            return "redirect:/vendor/tambah";
//        }
        Vendor vendor = vendorService.addVendor(vendorDTO);
//        List<Vendor> listVendor = vendorService.getAllVendors();
//        model.addAttribute("vendors", listVendor);
        return "viewall-vendor";

    }

    @GetMapping("/vendor/{kodeVendor}")
    public String detailVendor(@PathVariable("kodeVendor") String kodeVendor, Model model) {
        Vendor vendor = vendorService.getVendorDetail(kodeVendor); // Corrected to getVendorDetail
        if (vendor == null) {
            return "vendor-not-found";
        }
        model.addAttribute("vendor", vendor);
        return "view-detail-vendor";
    }
}