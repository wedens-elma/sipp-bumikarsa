package propensi.c06.sipp.service;

import java.util.List;
import java.util.Map;

import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;

public interface PengadaanService {
    List<Pengadaan> getAllPengadaan();
    Pengadaan addPengadaan(PengadaanRequestDTO pengadaanDto);
    Pengadaan getPengadaanDetail(String id);
    Map<String, Float> hitungTotalHarga(Pengadaan dto);
    void deletePengadaan(String kodePengadaan);
    //Pengadaan updatePengadaan(PengadaanRequestDTO pengadaanDto);
    //void updateStatusShipment(PengadaanRequestDTO pengadaanRequestDTO);
    //Pengadaan findById(String id);
    //void updatePengadaan(UpdatePengadaanRequestDTO pengadaanDto);
    Pengadaan update (Pengadaan pengadaan);
    void updateStatusPengadaan(Pengadaan pengadaan);
    public List<Pengadaan> getTop5LatestPengadaan();

}


