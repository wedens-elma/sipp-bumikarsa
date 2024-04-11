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
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.VendorService;

import java.util.List;

@Controller
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BarangService barangService;

    @GetMapping("/vendor")
    public String daftarVendor(Model model) {
        List<Vendor> listVendor = vendorService.getAllVendors();
        model.addAttribute("vendors", listVendor);
        return "viewall-vendor";
    }

    @GetMapping("/vendor/tambah")
    public String formTambahVendor(Model model) {
        model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
        model.addAttribute("listBarang", barangService.getAllBarang());
        return "form-tambah-vendor";
    }

    @PostMapping("/vendor/tambah")
    public String addVendor(@ModelAttribute("vendorDTO") CreateVendorRequestDTO vendorDTO, RedirectAttributes redirectAttributes) {
        try {
            Vendor vendor = vendorService.addVendor(vendorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor '" + vendor.getNamaVendor() + "' successfully added.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding vendor: " + e.getMessage());
        }
        return "redirect:/vendor";
    }

    @GetMapping("/vendor/{kodeVendor}")
    public String detailVendor(@PathVariable("kodeVendor") String kodeVendor, Model model, RedirectAttributes redirectAttributes) {
        Vendor vendor = vendorService.getVendorDetail(kodeVendor);
        if (vendor == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vendor not found.");
            return "redirect:/vendor";
        }
        model.addAttribute("vendor", vendor);
        return "view-detail-vendor";
    }
}
