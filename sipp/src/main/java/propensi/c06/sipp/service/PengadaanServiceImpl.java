package propensi.c06.sipp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.repository.BarangDb;
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



    @Override
    public Pengadaan updatePengadaan(Pengadaan pengadaanFromDto){
        Pengadaan pengadaan = getPengadaanDetail(pengadaanFromDto.getIdPengadaan());
        if(pengadaan != null){
            //pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
            pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
            pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
            pengadaan.setVendor(pengadaanFromDto.getVendor());
            pengadaan.setListPengadaanBarang(pengadaanFromDto.getListPengadaanBarang());
            pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
            pengadaan.setShipmentStatus(pengadaanFromDto.getShipmentStatus());
            pengadaan.setPaymentStatus(pengadaanFromDto.getPaymentStatus());
            pengadaanDb.save(pengadaan);
        }
        return pengadaan;
    }

    // Total Pengeluaran Perbulan
    public Map<String, Double> getTotalPengeluaranPerbulan() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll().stream()
                .filter(p -> !p.getIsDeleted())
                .collect(Collectors.toList());
        Map<String, Double> expenditurePerMonth = new HashMap<>();

        for (Pengadaan pengadaan : allPengadaans) {
            String month = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            Double total = pengadaan.getListPengadaanBarang().stream()
                    .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
                    .sum();
            expenditurePerMonth.merge(month, total, Double::sum);
        }

        return expenditurePerMonth;
    }

//    public Map<String, Double> getTotalPengeluaranPertahun() {
//        List<Pengadaan> allPengadaans = pengadaanDb.findAll();
//        Map<String, Double> expenditurePerYear = new HashMap<>();
//
//        for (Pengadaan pengadaan : allPengadaans) {
//            String year = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy"));
//            Double total = pengadaan.getListPengadaanBarang().stream()
//                    .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
//                    .sum();
//            expenditurePerYear.merge(year, total, Double::sum);
//        }
//
//        return expenditurePerYear;
//    }

    @Override
    public Map<String, Double> getTotalPengeluaranPertahun() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll();
        Map<String, Double> expenditurePerYear = new HashMap<>();

        for (Pengadaan pengadaan : allPengadaans) {
            String year = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy"));
            if (year.equals("2024")) {
                Double total = pengadaan.getListPengadaanBarang().stream()
                        .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
                        .sum();
                if (total >= 0) {
                    expenditurePerYear.merge(year, total, Double::sum);
                } else {
                    expenditurePerYear.merge(year, 0.0, Double::sum);
                }
            }
        }
        System.out.println(expenditurePerYear);
        return expenditurePerYear;
    }


    public int getTotalNumberOfPengadaans() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll();
        return allPengadaans.size();
    }




//    public Map<String, Double> getTotalPengeluaran() {
//        List<Pengadaan> allPengadaans = pengadaanDb.findAll();
//        Map<String, Double> expenditurePerMonth = new HashMap<>();
//
//        for (Pengadaan pengadaan : allPengadaans) {
//            String month = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy-MM"));
//            Double total = pengadaan.getListPengadaanBarang().stream()
//                    .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
//                    .sum();
//            expenditurePerMonth.merge(month, total, Double::sum);
//        }
//
//        return expenditurePerMonth;
//    }

//    @Override
//    public Pengadaan updatePengadaan(Pengadaan pengadaanFromDto) {
//        // Dapatkan entitas Pengadaan dari database berdasarkan ID
//        Optional<Pengadaan> pengadaanOptional = pengadaanDb.findById(pengadaanFromDto.getIdPengadaan());
//
//        if (pengadaanOptional.isPresent()) {
//            // Jika entitas ditemukan, perbarui propertinya dengan nilai dari DTO
//            Pengadaan pengadaanToUpdate = pengadaanOptional.get();
//            pengadaanToUpdate.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
//            pengadaanToUpdate.setTanggalPengadaan(pengadaanFromDto.getTanggalPengadaan());
//            pengadaanToUpdate.setVendor(pengadaanFromDto.getVendor());
//            pengadaanToUpdate.setListPengadaanBarang(pengadaanFromDto.getListPengadaanBarang());
//
//            // Simpan entitas yang diperbarui ke dalam database dan kembalikan
//            return pengadaanDb.save(pengadaanToUpdate);
//        } else {
//            // Jika entitas tidak ditemukan, Anda bisa menangani kasus ini sesuai kebutuhan aplikasi Anda.
//            // Misalnya, Anda bisa melemparkan exception atau mengembalikan nilai null.
//            throw new EntityNotFoundException("Pengadaan dengan ID " + pengadaanFromDto.getIdPengadaan() + " tidak ditemukan.");
//        }
//    }


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

    //nitip
//    //pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
//    //pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
//            pengadaan.setVendor(pengadaanFromDto.getVendor());
//            pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
//            pengadaan.setListPengadaanBarang(pengadaanFromDto.getListPengadaanBarang());
//nitip
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


//    @Override
//    public void updatePengadaan(String id, PengadaanRequestDTO pengadaanDto) {
//        Pengadaan pengadaanToUpdate = pengadaanDb.findById(id).orElseThrow(() -> new NoSuchElementException("Pengadaan not found"));
//
//        // Update data pengadaan dengan data baru dari DTO
//        pengadaanToUpdate.setNamaPengadaan(pengadaanDto.getNamaPengadaan());
//        pengadaanToUpdate.setTanggalPengadaan(LocalDate.parse(pengadaanDto.getTanggalPengadaan()));
//        pengadaanToUpdate.setVendor(pengadaanDto.getVendor());
//        pengadaanToUpdate.setPaymentStatus(pengadaanDto.getPaymentStatus());
//        pengadaanToUpdate.setShipmentStatus(pengadaanDto.getShipmentStatus());
//        pengadaanToUpdate.setDiskonKeseluruhan(pengadaanDto.getDiskonKeseluruhan());
//
//        // Hapus dulu semua barang pengadaan lama
//        pengadaanBarangDb.deleteAllById(pengadaanToUpdate.get);
//
//        // Tambahkan barang pengadaan baru dari DTO
//        for (PengadaanRequestDTO.PengadaanBarangDTO barangDto : pengadaanDto.getListBarang()) {
//            PengadaanBarang pengadaanBarang = new PengadaanBarang();
//            pengadaanBarang.setJumlahBarang(barangDto.getJumlahBarang());
//            pengadaanBarang.setHargaBarang(barangDto.getHargaBarang());
//            pengadaanBarang.setDiskonSatuan(barangDto.getDiskonSatuan());
//            pengadaanBarang.setBarang(barangDto.getBarang());
//            pengadaanBarang.setPengadaan(pengadaanToUpdate);
//            pengadaanBarang.setNamaBarang(barangDto.getBarang().getNamaBarang());
//
//            pengadaanBarangDb.save(pengadaanBarang);
//        }
//    }


}
