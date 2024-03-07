// package propensi.c06.sipp.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import propensi.c06.sipp.model.LogRencana;
// import propensi.c06.sipp.service.LogRencanaService;
// import java.util.List;

// @Controller
// public class LogRencanaController {
//     @Autowired
//     private LogRencanaService logRencanaService;
    
//     @GetMapping("/logrencana")
//     public String daftarLogRencana(Model model) {
//         List<LogRencana> listLogRencana = logRencanaService.getAllLogRencana();
//         model.addAttribute("listLogRencana", listLogRencana);
//         return "daftar-log-rencana";
//     }
// }
