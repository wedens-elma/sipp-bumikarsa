package propensi.c06.sipp.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.repository.PengadaanBarangDb;
import propensi.c06.sipp.repository.PengadaanDb;
import propensi.c06.sipp.repository.RencanaDb;
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

    @Autowired
    private RencanaDb rencanaDb;

    @Override
    public List<Pengadaan> getAllPengadaan(){
        return pengadaanDb.findAll();
    }

    @Override
    public Pengadaan addPengadaan(PengadaanRequestDTO pengadaanDto, Rencana rencana){
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

        if (rencana != null) {
            pengadaan.setRencana(rencana);
            rencana.setPengadaan(pengadaan);
            rencanaDb.save(rencana);
        } else {
            pengadaan.setRencana(null);
        }

        pengadaanDb.save(pengadaan);

        for (PengadaanBarang pengadaanBarangDTO : pengadaanDto.getListPengadaanBarang()){
            PengadaanBarang pbarangexisted = pengadaanBarangDb.findByPengadaanAndBarang(pengadaan, pengadaanBarangDTO.getBarang());
            if(pbarangexisted != null){
                pbarangexisted.setJumlahBarang(pbarangexisted.getJumlahBarang()+pengadaanBarangDTO.getJumlahBarang());
                pengadaanBarangDb.save(pbarangexisted);
            } else{
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
    public Long countPaymentStatus(String status) {
        List<Pengadaan> listPengadaan = pengadaanDb.findAll();
        Long count = 0L;
        int statusInt;
        
        if (status.equalsIgnoreCase("belum dibayar")) {
            statusInt = 0;
        } else if (status.equalsIgnoreCase("sudah dibayar")) {
            statusInt = 2;
        } else {
            statusInt = 1;
        }

        for (Pengadaan pengadaan : listPengadaan) {
            if (pengadaan.getPaymentStatus() == statusInt) {
                count += 1;
            }
        }

        return count;
    }

    @Override
    public Long countShipmentStatus(String status) {
        List<Pengadaan> listPengadaan = pengadaanDb.findAll();
        Long count = 0L;
        int statusInt;
        
        if (status.equalsIgnoreCase("sedang diproses")) {
            statusInt = 0;
        } else {
            statusInt = 1;
        }

        for (Pengadaan pengadaan : listPengadaan) {
            if (pengadaan.getShipmentStatus() == statusInt) {
                count += 1;
            }
        }

        return count;
    }

    @Override
    public Long countAllPengadaan() {
        return pengadaanDb.count();
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
        }else{
            List<PengadaanBarang> listPengadaanBarang = pengadaan.getListPengadaanBarang();
            // Iterasi melalui setiap PengadaanBarang dalam daftar
            for (PengadaanBarang pengadaanBarang : listPengadaanBarang) {
                Barang barang = pengadaanBarang.getBarang();
                int jumlahBarang = pengadaanBarang.getJumlahBarang();
                // Tambahkan jumlah barang ke stok barang terkait
                barang.setStokBarang(barang.getStokBarang() - jumlahBarang);
                // Simpan perubahan pada barang
                barangDb.save(barang);
            }
        }
        pengadaanDb.save(pengadaan);
    }

//    @Override
//    public Pengadaan updatePengadaan(UpdatePengadaanRequestDTO pengadaanFromDto) {
//        Pengadaan pengadaan = getPengadaanDetail(pengadaanFromDto.getIdPengadaan());
//
//        pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
//        pengadaan.setTanggalPengadaan(LocalDate.parse(pengadaanFromDto.getTanggalPengadaan()));
//        pengadaan.setVendor(pengadaanFromDto.getVendor());
//        pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
//        pengadaanDb.save(pengadaan);
//
//
//        List<PengadaanBarang> existingPengadaanBarangs = pengadaan.getListPengadaanBarang();
//        List<PengadaanBarang> updatedPengadaanBarangs = new ArrayList<>();
//
//        for (PengadaanBarang pengadaanBarangDTO : pengadaanFromDto.getListBarang()) {
//            PengadaanBarang pengadaanBarang = new PengadaanBarang();
//            Barang barang = barangService.getBarangById(pengadaanBarangDTO.getBarang().getKodeBarang());
//
//            pengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
//            pengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
//            pengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
//            pengadaanBarang.setBarang(barang);
//            pengadaanBarang.setPengadaan(pengadaan);
//            pengadaanBarang.setNamaBarang(barang.getNamaBarang());
//
//            boolean isExisting = false;
//            for (int i = 0; i < existingPengadaanBarangs.size(); i++) {
//                PengadaanBarang existingPengadaanBarang = existingPengadaanBarangs.get(i);
//                if (existingPengadaanBarang.getIdPengadaanBarang().equals(pengadaanBarangDTO.getIdPengadaanBarang())) {
//                    existingPengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
//                    existingPengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
//                    existingPengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
//                    existingPengadaanBarang.setBarang(barang);
//                    existingPengadaanBarang.setNamaBarang(barang.getNamaBarang());
//
//                    updatedPengadaanBarangs.add(existingPengadaanBarang);
//                    isExisting = true;
//                    break;
//                }
//            }
//
//            if (!isExisting) {
//                updatedPengadaanBarangs.add(pengadaanBarang);
//            }
//        }
//
//        for (PengadaanBarang existingPengadaanBarang : existingPengadaanBarangs) {
//            if (!updatedPengadaanBarangs.contains(existingPengadaanBarang)) {
//                pengadaanBarangDb.delete(existingPengadaanBarang);
//            }
//        }
//
//        pengadaan.setListPengadaanBarang(new ArrayList<>());
//
//        pengadaan.setListPengadaanBarang(updatedPengadaanBarangs);
//        pengadaanDb.save(pengadaan);
//
//        return pengadaan;
//    }

    @Override
    public Pengadaan updatePengadaan(Pengadaan pengadaanFromDto) {
        Pengadaan pengadaan = getPengadaanDetail(pengadaanFromDto.getIdPengadaan());
        System.out.println("id nyaaa ini" + pengadaanFromDto.getIdPengadaan());
        //pengadaan.setIdPengadaan(pengadaanFromDto.getIdPengadaan());
        pengadaan.setNamaPengadaan(pengadaanFromDto.getNamaPengadaan());
        pengadaan.setTanggalPengadaan(pengadaanFromDto.getTanggalPengadaan());
        pengadaan.setVendor(pengadaanFromDto.getVendor());
        pengadaan.setDiskonKeseluruhan(pengadaanFromDto.getDiskonKeseluruhan());
        pengadaanDb.save(pengadaan);


        List<PengadaanBarang> existingPengadaanBarangs = pengadaan.getListPengadaanBarang();
        List<PengadaanBarang> updatedPengadaanBarangs = new ArrayList<>();

        for (PengadaanBarang pengadaanBarangDTO : pengadaanFromDto.getListPengadaanBarang()) {
            PengadaanBarang pengadaanBarang = new PengadaanBarang();
            Barang barang = barangService.getBarangById(pengadaanBarangDTO.getBarang().getKodeBarang());

            pengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
            pengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
            pengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
            pengadaanBarang.setBarang(barang);
            pengadaanBarang.setPengadaan(pengadaan);
            pengadaanBarang.setNamaBarang(barang.getNamaBarang());

            boolean isExisting = false;
            for (int i = 0; i < existingPengadaanBarangs.size(); i++) {
                PengadaanBarang existingPengadaanBarang = existingPengadaanBarangs.get(i);
                if (existingPengadaanBarang.getIdPengadaanBarang().equals(pengadaanBarangDTO.getIdPengadaanBarang())) {
                    existingPengadaanBarang.setJumlahBarang(pengadaanBarangDTO.getJumlahBarang());
                    existingPengadaanBarang.setHargaBarang(pengadaanBarangDTO.getHargaBarang());
                    existingPengadaanBarang.setDiskonSatuan(pengadaanBarangDTO.getDiskonSatuan());
                    existingPengadaanBarang.setBarang(barang);
                    existingPengadaanBarang.setNamaBarang(barang.getNamaBarang());

                    updatedPengadaanBarangs.add(existingPengadaanBarang);
                    isExisting = true;
                    break;
                }
            }

            if (!isExisting) {
                updatedPengadaanBarangs.add(pengadaanBarang);
            }
        }

        for (PengadaanBarang existingPengadaanBarang : existingPengadaanBarangs) {
            if (!updatedPengadaanBarangs.contains(existingPengadaanBarang)) {
                pengadaanBarangDb.delete(existingPengadaanBarang);
            }
        }

        pengadaan.setListPengadaanBarang(new ArrayList<>());

        pengadaan.setListPengadaanBarang(updatedPengadaanBarangs);
        pengadaanDb.save(pengadaan);

        return pengadaan;
    }

    @Override
    public Map<String, Double> getTotalPengeluaranPertahun() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll().stream()
                .filter(p -> !p.getIsDeleted() && p.getPaymentStatus() == 2)
                .collect(Collectors.toList());
        Map<String, Double> expenditurePerYear = new HashMap<>();

        for (Pengadaan pengadaan : allPengadaans) {
            String year = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy"));
            if (year.equals("2024")) {
                Double total = pengadaan.getListPengadaanBarang().stream()
                        .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
                        .sum();
                expenditurePerYear.merge(year, total, Double::sum);
            }
        }
        System.out.println(expenditurePerYear);
        return expenditurePerYear;
    }

    @Override
    public Map<String, Double> getTotalPengeluaranPerbulan() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll().stream()
                .filter(p -> !p.getIsDeleted() && p.getPaymentStatus() == 2) // Check if the payment status is 'Paid'
                .collect(Collectors.toList());
        Map<String, Double> expenditurePerMonth = new HashMap<>();

        for (Pengadaan pengadaan : allPengadaans) {
            String month = pengadaan.getTanggalPengadaan().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            Double total = pengadaan.getListPengadaanBarang().stream()
                    .mapToDouble(pb -> pb.getHargaBarang() * pb.getJumlahBarang())
                    .sum();
            expenditurePerMonth.merge(month, total, Double::sum);
        }
        System.out.println(expenditurePerMonth);
        return expenditurePerMonth;
    }

    public int getTotalNumberOfPengadaans() {
        List<Pengadaan> allPengadaans = pengadaanDb.findAll();
        System.out.println(allPengadaans.size());
        return allPengadaans.size();
    }

    @Override
    public List<Pengadaan> getTop5LatestPengadaan() {
        return pengadaanDb.findTop5ByOrderByTanggalPengadaanDesc();
    }
}
