package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;

import java.util.List;
import java.util.Map;

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
    Pengadaan updatePengadaan(UpdatePengadaanRequestDTO pengadaanFromDto);
    void updateStatusPengadaan(Pengadaan pengadaan);
    //void updatePengadaan(String id, PengadaanRequestDTO dto);
}


