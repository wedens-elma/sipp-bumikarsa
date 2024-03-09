package propensi.c06.sipp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.service.RencanaService;

import java.util.List;

@RestController
@RequestMapping("/api/rencana")
public class RencanaRestController {
    @Autowired
    private RencanaService rencanaService;

    @GetMapping("/view-all")
    public List<Rencana> retrieveAllCategory(){
        return rencanaService.getAllRencana();
    }

    @GetMapping(value = "/{id}")
    public Rencana detailRencana(@PathVariable(value = "id") Long id, Model model) {
        return rencanaService.getRencanaById(id);
    }
}