package propensi.c06.sipp.service;

import java.util.List;
import propensi.c06.sipp.dto.request.CreateRencanaRequestDTO;
import propensi.c06.sipp.model.Rencana;

public interface RencanaService {
    List<Rencana> getAllRencana();

    Rencana getRencanaById(Long id);

    Rencana saveRencana(CreateRencanaRequestDTO rencanaDTO);

    void deleteRencana(Rencana rencana);

    void ubahStatusRencana(Rencana rencana, String status, String feedback);
}
