package propensi.c06.sipp.service;

import java.util.List;
import propensi.c06.sipp.model.Rencana;

public interface RencanaService {
    List<Rencana> getAllRencana();

    Rencana getRencanaById(Long id);

    void saveRencana(Rencana rencana);
}
