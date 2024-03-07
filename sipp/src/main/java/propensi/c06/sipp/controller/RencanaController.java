package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.service.RencanaService;
import java.util.List;

@Controller
public class RencanaController {
    @Autowired
    private RencanaService rencanaService;
    
    @GetMapping("/rencana")
    public String daftarRencana(Model model) {
        List<Rencana> listRencana = rencanaService.getAllRencana();
        model.addAttribute("listRencana", listRencana);
        return "daftar-rencana";
    }
    
}
