package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;

import java.util.List;
import java.util.Map;

public interface PengadaanService {
    List<Pengadaan> getAllPengadaan();
    Pengadaan addPengadaan(PengadaanRequestDTO pengadaanDto);
    Pengadaan getPengadaanDetail(String id);
    Map<String, Float> hitungTotalHarga(Pengadaan dto);
    Long countPaymentStatus(String status);
    Long countShipmentStatus(String status);
    Long countAllPengadaan();
}


