package propensi.c06.sipp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.service.LogRencanaService;
import propensi.c06.sipp.service.RencanaService;
import java.util.List;

@Controller
public class RencanaController {
    @Autowired
    private RencanaService rencanaService;

    @Autowired
    private LogRencanaService logRencanaService;
    
    @GetMapping("/rencana")
    public String daftarRencana(Model model) {
        List<Rencana> listRencana = rencanaService.getAllRencana();
        List<LogRencana> listLogRencana = logRencanaService.getAllLogRencana();
        model.addAttribute("listLogRencana", listLogRencana);
        model.addAttribute("listRencana", listRencana);
        return "daftar-rencana";
    }

    @GetMapping(value = "/rencana/{id}")
    public String detailRencana(@PathVariable(value = "id") Long id, Model model) {
        Rencana rencana = rencanaService.getRencanaById(id);
        model.addAttribute("rencana", rencana);
        return "detail-rencana";
    }

    
}
