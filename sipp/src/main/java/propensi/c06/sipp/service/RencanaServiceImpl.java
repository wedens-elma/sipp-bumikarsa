package propensi.c06.sipp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.c06.sipp.dto.request.CreateRencanaRequestDTO;
import propensi.c06.sipp.model.BarangRencana;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.BarangRencanaDb;
import propensi.c06.sipp.repository.LogRencanaDb;
import propensi.c06.sipp.repository.RencanaDb;

@Service
public class RencanaServiceImpl implements RencanaService {
    @Autowired
    RencanaDb rencanaDb;

    @Autowired
    BarangRencanaDb barangRencanaDb;

    @Autowired
    LogRencanaDb logRencanaDb;

    @Autowired
    UserService userService;

    @Autowired
    LogRencanaService logRencanaService;

    @Override
    public List<Rencana> getAllRencana() {
        List<Rencana> listRencanaAvail = new ArrayList<>();
        for (Rencana rencana : rencanaDb.findAll()) {
            if (!rencana.getIsDeleted()) {
                listRencanaAvail.add(rencana);
            }
        }
        return listRencanaAvail;
    }

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
    public void deleteRencana(Rencana rencana) {
        logRencanaService.createLogRencana(rencana, "dihapus", null);
        rencana.setLatestStatus("dihapus");
        rencana.setIsDeleted(true);
        rencanaDb.save(rencana);
    }

    @Override
    public void ubahStatusRencana(Rencana rencana, String status, String feedback) {
        logRencanaService.createLogRencana(rencana, status, feedback);
        rencana.setLatestStatus(status);
        rencanaDb.save(rencana);
    }

    @Override
    public Rencana saveRencana(CreateRencanaRequestDTO rencanaDTO) { 
        if (rencanaDTO.getListBarangRencana() == null) {
            return null;
        } else {
            Rencana rencana = new Rencana();
            rencana.setVendor(rencanaDTO.getVendor());
            rencana.setCreatedBy(userService.getCurrentUserName());
            rencana.setNamaRencana(rencanaDTO.getNamaRencana());
            rencana.setExpectedDate(LocalDate.parse(rencanaDTO.getExpectedDate()));
            rencana.setLatestStatus("dibuat");
            rencanaDb.save(rencana);

            for (CreateRencanaRequestDTO.BarangRencanaDTO barangRencanaDTO : rencanaDTO.getListBarangRencana()) {
                BarangRencana existingBarangRencana = barangRencanaDb.findByRencanaAndBarang(rencana, barangRencanaDTO.getBarang());
                if (existingBarangRencana != null) {
                    existingBarangRencana.setKuantitas(existingBarangRencana.getKuantitas() + barangRencanaDTO.getKuantitas());
                    barangRencanaDb.save(existingBarangRencana);
                } else {
                    BarangRencana newBarangRencana = new BarangRencana();
                    newBarangRencana.setKuantitas(barangRencanaDTO.getKuantitas());
                    newBarangRencana.setBarang(barangRencanaDTO.getBarang());
                    newBarangRencana.setRencana(rencana);
                    newBarangRencana.setNamaBarang(barangRencanaDTO.getBarang().getNamaBarang());
                    barangRencanaDb.save(newBarangRencana);
                }
            }
            LogRencana logRencana = new LogRencana();
            logRencana.setRencana(rencana);
            logRencana.setStatus("dibuat");
            logRencana.setTanggalWaktu(LocalDateTime.now());
            logRencana.setChangedBy(userService.getCurrentUserName());
            rencana.setLogRencana(new ArrayList<LogRencana>());
            rencana.getLogRencana().add(logRencana);

            logRencanaDb.save(logRencana);
            return rencana;
        }
    }
}
