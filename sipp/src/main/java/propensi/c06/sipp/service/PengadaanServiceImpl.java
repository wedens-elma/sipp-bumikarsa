package propensi.c06.sipp.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.repository.PengadaanBarangDb;
import propensi.c06.sipp.repository.PengadaanDb;

@Service
@Transactional
public class PengadaanServiceImpl implements PengadaanService {

    @Autowired
    private PengadaanDb pengadaanDb;

    @Autowired
    private PengadaanBarangDb pengadaanBarangDb;
    
    @Autowired
    private BarangService barangService;

    @Override
    public List<Pengadaan> getAllPengadaan(){
        return pengadaanDb.findAll();
    }

    @Override
    public Pengadaan addPengadaan(PengadaanRequestDTO pengadaanDto){
        Pengadaan pengadaan = new Pengadaan();


        String idPengadaan = "PR-";
        String nextNumericId = pengadaanDb.findCodePengadaan(idPengadaan);
        if(nextNumericId == null){
            idPengadaan+="001";
        } else{
            int number = Integer.parseInt(nextNumericId.substring(3,6))+1;
            if (number < 10){
                idPengadaan = idPengadaan + "00" + number;
            }else if (number<100){
                idPengadaan = idPengadaan+ "0" + number;
            }else{
                idPengadaan+=number;
            }
        }
        pengadaan.setIdPengadaan(idPengadaan);

        pengadaan.setNamaPengadaan(pengadaanDto.getNamaPengadaan());
        pengadaan.setTanggalPengadaan(LocalDate.parse(pengadaanDto.getTanggalPengadaan()));
        pengadaan.setVendor(pengadaanDto.getVendor());
        pengadaan.setPaymentStatus(pengadaanDto.getPaymentStatus());
        pengadaan.setShipmentStatus(pengadaanDto.getShipmentStatus());
        pengadaan.setDiskonKeseluruhan(pengadaanDto.getDiskonKeseluruhan());
        pengadaanDb.save(pengadaan);

        for (PengadaanBarang pengadaanBarangDTO : pengadaanDto.getListBarang()){
            PengadaanBarang pengadaanBarang = new PengadaanBarang();
            var barang = barangService.getBarangById(pengadaanBarangDTO.getBarang().getKodeBarang());

            pengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
            pengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
            pengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
            pengadaanBarang.setBarang (pengadaanBarangDTO.getBarang());
            pengadaanBarang.setPengadaan(pengadaan);
            pengadaanBarang.setNamaBarang(barang.getNamaBarang());

            pengadaanBarangDb.save(pengadaanBarang);
        }

        return pengadaan;
    }

    @Override
    public Pengadaan getPengadaanDetail(String id) {
        for (Pengadaan pengadaan : getAllPengadaan()) {
            if (pengadaan.getIdPengadaan().equals(id)) {
                return pengadaan;
            }
        }
        return null;

    }

    @Override
    public Map<String, Float> hitungTotalHarga(Pengadaan dto) {
        float totalHargaAwal = 0.0f;
        float totalHargaDiskonSatuan = 0.0f;

        for (PengadaanBarang x : dto.getListPengadaanBarang()) {
            // Hitung totalHargaAwal
            float totalHargaBarangAwal = x.getJumlahBarang() * x.getHargaBarang();
            totalHargaAwal += totalHargaBarangAwal;

            // Hitung totalHargaDiskonSatuan
            float disSatuan = x.getDiskonSatuan();
            float totalHargaBarangDiskonSatuan = totalHargaBarangAwal - (totalHargaBarangAwal * (disSatuan / 100));

            totalHargaDiskonSatuan += totalHargaBarangDiskonSatuan;
        }

        float disKeseluruhan = dto.getDiskonKeseluruhan();
        float totalHargaAkhir = totalHargaDiskonSatuan - (totalHargaDiskonSatuan * (disKeseluruhan / 100));

        Map<String, Float> result = new HashMap<>();
        result.put("totalHargaAwal", totalHargaAwal);
        result.put("totalHargaDiskonSatuan", totalHargaDiskonSatuan);
        result.put("totalHargaAkhir", totalHargaAkhir);

        return result;
    }

}
