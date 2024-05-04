package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.LogPengadaan;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.repository.LogPengadaanDb;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogPengadaanServiceImpl implements LogPengadaanService {
    @Autowired
    LogPengadaanDb logPengadaanDb;

    @Autowired
    UserService userService;

    @Override
    public List<LogPengadaan> getAllLogPengadaan(){
        return logPengadaanDb.findAll();
    }

    @Override
    public void createLogPengadaan(Pengadaan pengadaan, String status, String feedback){
        LogPengadaan logPengadaan = new LogPengadaan();
        logPengadaan.setPengadaan(pengadaan);
        logPengadaan.setChangedBy(userService.getCurrentUserName());
        logPengadaan.setStatus(status);
        logPengadaan.setTanggalWaktu(LocalDateTime.now());
        logPengadaan.setFeedback(feedback);
        pengadaan.getLogPengadan().add(logPengadaan);
        logPengadaanDb.save(logPengadaan);
    }

}
