package propensi.c06.sipp.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.LogRencanaDb;

@Service
public class LogRencanaServiceImpl implements LogRencanaService {
    @Autowired
    LogRencanaDb logRencanaDb;

    @Autowired
    UserService userService;

    @Override
    public List<LogRencana> getAllLogRencana() { return logRencanaDb.findAll(); }

    @Override
    public void createLogRencana(Rencana rencana, String status, String feedback) {
        LogRencana logRencana = new LogRencana();
        logRencana.setRencana(rencana);
        logRencana.setChangedBy(userService.getCurrentUserName());
        logRencana.setStatus(status);
        logRencana.setFeedback(feedback);
        logRencana.setTanggalWaktu(LocalDateTime.now());
        rencana.getLogRencana().add(logRencana);
        logRencanaDb.save(logRencana);
    }

}
