package propensi.c06.sipp.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.c06.sipp.dto.BarangMapper;
import propensi.c06.sipp.dto.VendorMapper;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.request.UpdateVendorRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.UserService;
import propensi.c06.sipp.service.VendorService;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BarangService barangService;


    @Autowired
    VendorMapper vendorMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/vendor")
    public String daftarVendor(Model model, @RequestParam(value = "search", required = false) String search) {
        List<Vendor> listVendor;
        if (search != null && !search.isEmpty()) {
            listVendor = vendorService.searchVendors(search);
        } else {
            listVendor = vendorService.getAllVendors();
        }
        model.addAttribute("vendors", listVendor);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        return "viewall-vendor";
    }



    @GetMapping("/vendor/tambah")
    public String formTambahVendor(Model model) {
        model.addAttribute("vendorDTO", new CreateVendorRequestDTO());
        model.addAttribute("listBarang", barangService.getAllBarang());
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        return "form-tambah-vendor";
    }

    @PostMapping("/vendor/tambah")
    public String addVendor(@ModelAttribute("vendorDTO") CreateVendorRequestDTO vendorDTO, RedirectAttributes redirectAttributes) {
        try {
            Vendor vendor = vendorService.addVendor(vendorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor '" + vendor.getNamaVendor() + "' successfully added.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding vendor: " + e.getMessage());
        }
        return "redirect:/vendor";
    }


//    @GetMapping("/vendor/{kodeVendor}")
//    public String detailVendor(@PathVariable("kodeVendor") String kodeVendor, Model model, RedirectAttributes redirectAttributes) {
//        Vendor vendor = vendorService.getVendorDetail(kodeVendor);
//        if (vendor == null) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Vendor not found.");
//            return "redirect:/vendor";
//        }
//        String username = userService.getCurrentUserName();
//        model.addAttribute("username", username);
//        model.addAttribute("vendor", vendor);
//        return "view-detail-vendor";
//    }

    @GetMapping("/vendor/{kodeVendor}")
    public String detailVendor(@PathVariable("kodeVendor") String kodeVendor, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "search", required = false) String search) {
        Vendor vendor = vendorService.getVendorDetail(kodeVendor);
        if (vendor == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vendor not found.");
            return "redirect:/vendor";
        }
        List<Barang> barangList = (search == null || search.isEmpty()) ? vendor.getBarangList() : barangService.searchBarangByName(search);
        model.addAttribute("username", userService.getCurrentUserName());
        model.addAttribute("vendor", vendor);
        model.addAttribute("barangList", barangList);
        return "view-detail-vendor";
    }


    @GetMapping("/vendor/{kodeVendor}/update")
    public String formUpdateVendor(@PathVariable String kodeVendor, Model model, RedirectAttributes redirectAttributes) {
        Vendor vendor = vendorService.getVendorDetail(kodeVendor);
        if (vendor == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vendor not found.");
            return "redirect:/vendor";
        }
        UpdateVendorRequestDTO vendorDTO = vendorMapper.vendorToUpdateVendorRequestDTO(vendor);
        String username = userService.getCurrentUserName();
        model.addAttribute("username", username);
        model.addAttribute("vendorDTO", vendorDTO);
        model.addAttribute("allBarang", barangService.getAllBarang());
        return "form-update-vendor";
    }

    @PostMapping("/vendor/{kodeVendor}/update")
    public String updateVendor(@PathVariable String kodeVendor, @Valid @ModelAttribute("vendorDTO") UpdateVendorRequestDTO vendorDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("allBarang", barangService.getAllBarang());
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the form fields.");
            return "redirect:/vendor/" + kodeVendor + "/update";
        }

        try {
            Vendor updatedVendor = vendorService.updateVendor(vendorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor '" + updatedVendor.getNamaVendor() + "' successfully updated.");
            redirectAttributes.addAttribute("kodeVendor", updatedVendor.getKodeVendor()); // Add 'kodeVendor' attribute for use in the success view
            return "redirect:/success-update-vendor";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/vendor/" + kodeVendor + "/update";
        }
    }

    @GetMapping("/success-update-vendor")
    public String showUpdateVendorSuccess(@RequestParam("kodeVendor") String kodeVendor, Model model) {
        model.addAttribute("kodeVendor", kodeVendor);
        return "success-update-vendor";
    }


//    @PostMapping("/vendor/{kodeVendor}/update")
//    public String updateVendor(@PathVariable String kodeVendor, @Valid @ModelAttribute("vendorDTO") UpdateVendorRequestDTO vendorDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("allBarang", barangService.getAllBarang());
//            model.addAttribute("errorMessage", "Please correct the form fields.");
//            return "form-update-vendor";
//        }
//
//        try {
//            Vendor updatedVendor = vendorService.updateVendor(vendorDTO);
//            redirectAttributes.addFlashAttribute("successMessage", "Vendor '" + updatedVendor.getNamaVendor() + "' successfully updated.");
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
//            return "redirect:/vendor/" + kodeVendor + "/update";
//        }
//
//        return "redirect:/vendor";
//    }

//
//

    @GetMapping("/vendor/{kodeVendor}/delete")
    public String softDeleteVendor(@PathVariable("kodeVendor") String kodeVendor, Model model) {
        Vendor vendor = vendorService.getVendorDetail(kodeVendor);
        if (vendor != null) {
            vendorService.softDeleteVendor(kodeVendor);
            model.addAttribute("namaVendor", vendor.getNamaVendor());
            model.addAttribute("kodeVendor", kodeVendor);
            String username = userService.getCurrentUserName();
            model.addAttribute("username", username);
            return "success-delete-vendor";
        } else {
            return "redirect:/vendor";
        }
    }

}
