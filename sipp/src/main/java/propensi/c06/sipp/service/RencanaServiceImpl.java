package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.RencanaDb;
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
}
