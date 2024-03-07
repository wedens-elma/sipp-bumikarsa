package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.RencanaDb;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RencanaServiceImpl implements RencanaService {
    @Autowired
    RencanaDb rencanaDb;

    @Override
    public List<Rencana> getAllRencana() { return rencanaDb.findAll(); }

    @Override
    public Rencana getRencanaById(Long id) {
        for (Rencana rencana : getAllRencana()) {
            if (rencana.getIdRencana().equals(id)) {
                return rencana;
            }
        }
        return null;    
    }

    @Override
    public void saveRencana(Rencana rencana) { 
        LogRencana logRencana = new LogRencana();
        logRencana.setRencana(rencana);
        logRencana.setStatus("created");
        logRencana.setTanggalWaktu(LocalDateTime.now());
        logRencana.setUser(rencana.getUser());
        rencana.setLogRencana(new ArrayList<LogRencana>());
        rencana.getLogRencana().add(logRencana);
        rencanaDb.save(rencana); 
    }
}
