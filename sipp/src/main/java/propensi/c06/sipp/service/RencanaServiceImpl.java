package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.c06.sipp.dto.request.CreateRencanaRequestDTO;
import propensi.c06.sipp.model.BarangRencana;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.BarangRencanaDb;
import propensi.c06.sipp.repository.LogRencanaDb;
import propensi.c06.sipp.repository.RencanaDb;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RencanaServiceImpl implements RencanaService {
    @Autowired
    RencanaDb rencanaDb;

    @Autowired
    BarangRencanaDb barangRencanaDb;

    @Autowired
    LogRencanaDb logRencanaDb;

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
    public Rencana saveRencana(CreateRencanaRequestDTO rencanaDTO) { 
        Rencana rencana = new Rencana();
        rencana.setVendor(rencanaDTO.getVendor());
        rencana.setCreatedBy(rencanaDTO.getCreatedBy());
        rencana.setNamaRencana(rencanaDTO.getNamaRencana());
        rencana.setExpectedDate(LocalDate.parse(rencanaDTO.getExpectedDate()));
        rencanaDb.save(rencana); 

        for (CreateRencanaRequestDTO.BarangRencanaDTO barangRencanaDTO : rencanaDTO.getListBarangRencana()) {
            BarangRencana barangRencana = new BarangRencana();
            barangRencana.setKuantitas(barangRencanaDTO.getKuantitas());
            barangRencana.setBarang(barangRencana.getBarang());
            barangRencana.setRencana(rencana);
            barangRencanaDb.save(barangRencana);
        }

        LogRencana logRencana = new LogRencana();
        logRencana.setRencana(rencana);
        logRencana.setStatus("created");
        logRencana.setTanggalWaktu(LocalDateTime.now());
        logRencana.setChangedBy(rencana.getCreatedBy());
        rencana.setLogRencana(new ArrayList<LogRencana>());
        rencana.getLogRencana().add(logRencana);

        logRencanaDb.save(logRencana);
        return rencana;
    }
}
