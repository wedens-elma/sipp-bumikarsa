package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import propensi.c06.sipp.dto.RencanaMapper;
import propensi.c06.sipp.dto.request.CreateRencanaRequestDTO;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.LogRencanaService;
import propensi.c06.sipp.service.RencanaService;
import propensi.c06.sipp.service.VendorService;

import java.util.List;

@Controller
@RequestMapping("/rencana")
public class RencanaController {
    @Autowired
    private RencanaService rencanaService;

    @Autowired
    private LogRencanaService logRencanaService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private RencanaMapper rencanaMapper;
    
    @GetMapping("/")
    public String daftarRencana(Model model) {
        List<Rencana> listRencana = rencanaService.getAllRencana();
        List<LogRencana> listLogRencana = logRencanaService.getAllLogRencana();
        model.addAttribute("listLogRencana", listLogRencana);
        model.addAttribute("listRencana", listRencana);
        return "daftar-rencana";
    }

    @GetMapping(value = "/{id}")
    public String detailRencana(@PathVariable(value = "id") Long id, Model model) {
        Rencana rencana = rencanaService.getRencanaById(id);
        model.addAttribute("rencana", rencana);
        return "detail-rencana";
    }

    @GetMapping("/create")
    public String formAddRencana(Model model) {
        var rencanaDTO = new CreateRencanaRequestDTO();
        var listVendor = vendorService.getAllVendor();
        var listBarang = barangService.getAllBarang();
        model.addAttribute("rencanaDTO", rencanaDTO);
        model.addAttribute("listVendorExisted", listVendor);
        model.addAttribute("listBarangExisted", listBarang);
        return "form-create-rencana";
    }

    @PostMapping("/create")
    public String addRencana(@Valid @ModelAttribute CreateRencanaRequestDTO rencanaDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            model.addAttribute("errorMessage", errorMessage.toString());
            return "error-view";
        }
        var rencana = rencanaMapper.createRencanaRequestDTOToRencana(rencanaDTO);
        rencanaService.saveRencana(rencana);
        model.addAttribute("id", rencana.getIdRencana());
        model.addAttribute("nama", rencana.getNamaRencana());
        return "success-create-rencana";
    }
}
