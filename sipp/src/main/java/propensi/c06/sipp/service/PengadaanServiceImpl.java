package propensi.c06.sipp.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
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
        pengadaanDb.save(pengadaan);
    }


//    @Override
//    public Pengadaan updatePengadaan(PengadaanRequestDTO pengadaanDto){
//        Pengadaan pengadaan= pengadaanDb.findById(pengadaanDto.getIdPengadaan()).orElse(null);
//        if (pengadaan != null && pengadaan.getPaymentStatus()==0 && pengadaan.getShipmentStatus()==0){
//            pengadaan.setNamaPengadaan(pengadaanDto.getNamaPengadaan());
//            pengadaan.setTanggalPengadaan(LocalDate.parse(pengadaanDto.getTanggalPengadaan()));
//            pengadaan.setVendor(pengadaanDto.getVendor());
//            pengadaan.setPaymentStatus(pengadaanDto.getPaymentStatus());
//            pengadaan.setShipmentStatus(pengadaanDto.getShipmentStatus());
//            pengadaan.setDiskonKeseluruhan(pengadaanDto.getDiskonKeseluruhan());
//            deletePengadaan(pengadaan.getIdPengadaan());
//
//            for (PengadaanBarang pengadaanBarangDTO : pengadaanDto.getListBarang()) {
//                PengadaanBarang pengadaanBarang = new PengadaanBarang();
//                var barang = barangService.getBarangById(pengadaanBarangDTO.getBarang().getKodeBarang());
//
//                pengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
//                pengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
//                pengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
//                pengadaanBarang.setBarang(pengadaanBarangDTO.getBarang());
//                pengadaanBarang.setPengadaan(pengadaan);
//                pengadaanBarang.setNamaBarang(barang.getNamaBarang());
//
//                pengadaanBarangDb.save(pengadaanBarang);
//            }
//            return pengadaanDb.save(pengadaan);
//        } else {
//            throw new RuntimeException("Tidak bisa melakukan update jika Payment Status tidak Not Paid dan Shipement Status tidak In Progress");
//        }
//    }

//    @Override
//    public void updateStatusShipment(String id, int ShipmentStatus, int PaymentStatus) {
//        Pengadaan pengadaan = pengadaanDb.getReferenceById(id);
//        pengadaan.setShipmentStatus(ShipmentStatus);
//        pengadaan.setPaymentStatus(PaymentStatus);
//        pengadaanDb.save(pengadaan);
//    }

//    @Override
//    public Pengadaan updateStatusShipment(PengadaanRequestDTO pengadaanDto){
//        Pengadaan pengadaan = pengadaanDb.findById(pengadaanDto.getIdPengadaan()).orElse(null);
//        if (pengadaan != null) {
//            pengadaan.setNamaPengadaan(pengadaanDto.getNamaPengadaan());
//            pengadaan.setTanggalPengadaan(LocalDate.parse(pengadaanDto.getTanggalPengadaan()));
//            pengadaan.setVendor(pengadaanDto.getVendor());
//            pengadaan.setPaymentStatus(pengadaanDto.getPaymentStatus());
//            pengadaan.setShipmentStatus(pengadaanDto.getShipmentStatus());
//            pengadaan.setDiskonKeseluruhan(pengadaanDto.getDiskonKeseluruhan());
//
//            pengadaan.getListPengadaanBarang().clear();
//            for (PengadaanBarang pengadaanBarangDTO : pengadaanDto.getListBarang()){
//                PengadaanBarang pengadaanBarang = new PengadaanBarang();
//                var barang = barangService.getBarangById(pengadaanBarangDTO.getBarang().getKodeBarang());
//
//                pengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
//                pengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
//                pengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
//                pengadaanBarang.setBarang(pengadaanBarangDTO.getBarang());
//                pengadaanBarang.setPengadaan(pengadaan);
//                pengadaanBarang.setNamaBarang(barang.getNamaBarang());
//
//                pengadaan.getListPengadaanBarang().add(pengadaanBarang);
//            }
//
//            return pengadaanDb.save(pengadaan);
//        } else {
//            return null;
//        }
//    }

//
//    @Override
//    public Pengadaan findById(String id) {
//        return pengadaanDb.findById(id).orElse(null);
//    }


//    @Override
//    public void updatePengadaan(UpdatePengadaanRequestDTO pengadaanDto) {
//        // Dapatkan pengadaan yang akan diperbarui
//        Pengadaan pengadaanToUpdate = pengadaanDb.findById(pengadaanDto.getIdPengadaan()).orElse(null);
//        if (pengadaanToUpdate == null) {
//            throw new RuntimeException("Pengadaan not found with id: " + pengadaanDto.getIdPengadaan());
//        }
//
//        // Update informasi pengadaan
//        pengadaanToUpdate.setNamaPengadaan(pengadaanDto.getNamaPengadaan());
//        pengadaanToUpdate.setTanggalPengadaan(LocalDate.parse(pengadaanDto.getTanggalPengadaan()));
//        pengadaanToUpdate.setVendor(pengadaanDto.getVendor());
//        pengadaanToUpdate.setDiskonKeseluruhan(pengadaanDto.getDiskonKeseluruhan());
//
////        // Hapus semua barang terkait pengadaan yang akan diperbarui
////        pengadaanDb.deleteById(pengadaanToUpdate.getIdPengadaan());
//
//        // Tambahkan barang-barang yang baru dari DTO
//        List<PengadaanBarang> listPengadaanBarang = new ArrayList<>();
//        for (UpdatePengadaanRequestDTO.PengadaanBarangDTO barangDTO : pengadaanDto.getListBarang()) {
//            PengadaanBarang pengadaanBarang = new PengadaanBarang();
//            pengadaanBarang.setJumlahBarang(barangDTO.getJumlahBarang());
//            pengadaanBarang.setHargaBarang(barangDTO.getHargaBarang());
//            pengadaanBarang.setDiskonSatuan(barangDTO.getDiskonSatuan());
//            pengadaanBarang.setBarang(barangDTO.getBarang());
//            pengadaanBarang.setPengadaan(pengadaanToUpdate);
//            listPengadaanBarang.add(pengadaanBarang);
//        }
//        pengadaanToUpdate.setListPengadaanBarang(listPengadaanBarang);
//
//        // Simpan perubahan
//        pengadaanDb.save(pengadaanToUpdate);
//    }


    @Override
    public Pengadaan update(Pengadaan pengadaan){
        String id = pengadaan.getIdPengadaan();
        Pengadaan dto = pengadaanDb.findById(id).get();
        dto.setTanggalPengadaan(pengadaan.getTanggalPengadaan());
        dto.setNamaPengadaan(pengadaan.getNamaPengadaan());
        dto.setVendor(pengadaan.getVendor());
        dto.setListPengadaanBarang(pengadaan.getListPengadaanBarang());
        dto.setDiskonKeseluruhan(pengadaan.getDiskonKeseluruhan());
        dto.setPaymentStatus(0);
        dto.setShipmentStatus(0);
        return pengadaanDb.save(dto);
    }

}
