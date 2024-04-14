package propensi.c06.sipp.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.PengadaanBarangDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.repository.PengadaanBarangDb;
import propensi.c06.sipp.repository.PengadaanDb;
import propensi.c06.sipp.repository.VendorDb;

@Service
@Transactional
public class PengadaanServiceImpl implements PengadaanService {

    @Autowired
    private PengadaanDb pengadaanDb;

    @Autowired
    private PengadaanBarangDb pengadaanBarangDb;

    @Autowired
    private VendorDb vendorDb;
    @Autowired
    private BarangDb barangDb;
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

        if(dto.getListPengadaanBarang() != null) {
            for (PengadaanBarang x : dto.getListPengadaanBarang()) {
                // Hitung totalHargaAwal
                float totalHargaBarangAwal = x.getJumlahBarang() * x.getHargaBarang();
                totalHargaAwal += totalHargaBarangAwal;

                // Hitung totalHargaDiskonSatuan
                float disSatuan = x.getDiskonSatuan();
                float totalHargaBarangDiskonSatuan = totalHargaBarangAwal - (totalHargaBarangAwal * (disSatuan / 100));

                totalHargaDiskonSatuan += totalHargaBarangDiskonSatuan;
            }
        }

        float disKeseluruhan = dto.getDiskonKeseluruhan();
        float totalHargaAkhir = totalHargaDiskonSatuan - (totalHargaDiskonSatuan * (disKeseluruhan / 100));

        Map<String, Float> result = new HashMap<>();
        result.put("totalHargaAwal", totalHargaAwal);
        result.put("totalHargaDiskonSatuan", totalHargaDiskonSatuan);
        result.put("totalHargaAkhir", totalHargaAkhir);

        return result;
    }

    @Override
    public void deletePengadaan(String kodePengadaan){
        Pengadaan pengadaan= pengadaanDb.getReferenceById(kodePengadaan);
        pengadaan.setIsDeleted(true);
        pengadaanDb.save(pengadaan);
    }

    @Override
    public void updateStatusPengadaan(Pengadaan pengadaan) {
        // Periksa apakah paymentStatus baru adalah 1
        if (pengadaan.getShipmentStatus() == 1) {
            List<PengadaanBarang> listPengadaanBarang = pengadaan.getListPengadaanBarang();
            // Iterasi melalui setiap PengadaanBarang dalam daftar
            for (PengadaanBarang pengadaanBarang : listPengadaanBarang) {
                Barang barang = pengadaanBarang.getBarang();
                int jumlahBarang = pengadaanBarang.getJumlahBarang();
                // Tambahkan jumlah barang ke stok barang terkait
                barang.setStokBarang(barang.getStokBarang() + jumlahBarang);
                // Simpan perubahan pada barang
                barangDb.save(barang);
            }
        }
        pengadaanDb.save(pengadaan);
    }



//    @Override
//    public Pengadaan updatePengadaan(Pengadaan pengadaanFromDto){
//        Pengadaan pengadaan = getPengadaanDetail(pengadaanFromDto.getIdPengadaan());
//
//        if(pengadaan != null){
//            //pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
//            pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
//            pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
//            pengadaan.setVendor(pengadaanFromDto.getVendor());
//            pengadaan.setListPengadaanBarang(pengadaanFromDto.getListPengadaanBarang());
//            pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
//            pengadaan.setShipmentStatus(pengadaanFromDto.getShipmentStatus());
//            pengadaan.setPaymentStatus(pengadaanFromDto.getPaymentStatus());
//            pengadaanDb.save(pengadaan);
//        }
//        return pengadaan;
//    }


    @Override
    public Pengadaan updatePengadaan(Pengadaan pengadaanFromDto) {
        Pengadaan pengadaan = getPengadaanDetail(pengadaanFromDto.getIdPengadaan());
        if (pengadaan != null) {
            // Memperbarui atribut Pengadaan dari DTO
            pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
            pengadaan.setTanggalPengadaan(pengadaanFromDto.getTanggalPengadaan());
            pengadaan.setVendor(pengadaanFromDto.getVendor());
            pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
            pengadaan.setShipmentStatus(pengadaanFromDto.getShipmentStatus());
            pengadaan.setPaymentStatus(pengadaanFromDto.getPaymentStatus());

            // Memperbarui list pengadaan barang
            List<PengadaanBarang> updatedListPengadaanBarang = new ArrayList<>();
            if (pengadaanFromDto.getListPengadaanBarang() != null) {
                for (PengadaanBarang barangDTO : pengadaanFromDto.getListPengadaanBarang()) {
                    PengadaanBarang pengadaanBarang = new PengadaanBarang();
                    pengadaanBarang.setJumlahBarang(barangDTO.getJumlahBarang());
                    pengadaanBarang.setHargaBarang(barangDTO.getHargaBarang());
                    pengadaanBarang.setDiskonSatuan(barangDTO.getDiskonSatuan());
                    pengadaanBarang.setBarang(barangDTO.getBarang());
                    updatedListPengadaanBarang.add(pengadaanBarang);
                }
            }
            pengadaan.setListPengadaanBarang(updatedListPengadaanBarang);

            // Simpan perubahan pada pengadaan
            return pengadaanDb.save(pengadaan);
        }
        return null; // Atau lakukan penanganan kesalahan lainnya sesuai kebutuhan aplikasi Anda
    }

}
