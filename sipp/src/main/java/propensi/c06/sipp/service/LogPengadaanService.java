package propensi.c06.sipp.service;

import propensi.c06.sipp.model.LogPengadaan;
import propensi.c06.sipp.model.Pengadaan;

import java.util.List;

public interface LogPengadaanService {
    List<LogPengadaan> getAllLogPengadaan();
    void createLogPengadaan(Pengadaan pengadaan, String status, String feedback);
}
