package propensi.c06.sipp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("listVendor", listVendor);
        return "viewall-vendor";
    }

    @GetMapping("/vendor/tambah")
    public String formTambahVendor(Model model) {
        model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
        return "form-tambah-vendor";
    }

    @PostMapping("/vendor/tambah")
    public String addVendor(@ModelAttribute CreateVendorRequestDTO vendorDTO, Model model) {
        vendorService.createVendor(vendorDTO);
        return "redirect:/vendor";
    }

    @GetMapping("/vendor/{kodeVendor}")
    public String detailVendor(@PathVariable("kodeVendor") String kodeVendor, Model model) {
        Vendor vendor = vendorService.getVendorByCode(kodeVendor);
        if (vendor == null) {
            return "vendor-not-found";
        }
        model.addAttribute("vendor", vendor);
        return "view-detail-vendor";
    }
}
