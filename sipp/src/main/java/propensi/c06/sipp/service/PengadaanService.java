package propensi.c06.sipp.service;

import java.util.List;
import java.util.Map;

import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.Rencana;

public interface PengadaanService {
    List<Pengadaan> getAllPengadaan();
    Pengadaan addPengadaan(PengadaanRequestDTO pengadaanDto, Rencana rencana);
    Pengadaan getPengadaanDetail(String id);
    Map<String, Float> hitungTotalHarga(Pengadaan dto);
    Long countPaymentStatus(String status);
    Long countShipmentStatus(String status);
    Long countAllPengadaan();
    void deletePengadaan(String kodePengadaan);
    //Pengadaan updatePengadaan(PengadaanRequestDTO pengadaanDto);
    //void updateStatusShipment(PengadaanRequestDTO pengadaanRequestDTO);
    //Pengadaan findById(String id);
    //void updatePengadaan(UpdatePengadaanRequestDTO pengadaanDto);
    Pengadaan updatePengadaan(UpdatePengadaanRequestDTO pengadaanFromDto);
    void updateStatusPengadaan(Pengadaan pengadaan);
    public List<Pengadaan> getTop5LatestPengadaan();

    Map<String, Double> getTotalPengeluaranPerbulan();

    Map<String, Double> getTotalPengeluaranPertahun();
    int getTotalNumberOfPengadaans();
    //void updatePengadaan(String id, PengadaanRequestDTO dto);
}


