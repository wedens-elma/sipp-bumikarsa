package propensi.c06.sipp.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.LogBarang;
import propensi.c06.sipp.repository.BarangDb;
import propensi.c06.sipp.repository.LogBarangDb;

@Service
public class BarangServiceImpl implements BarangService {
    @Autowired
    private UserService userService;

    @Autowired
    BarangDb barangDb;

    @Autowired
    LogBarangDb logBarangDb;

    @Override
    public void addBarang(Barang barang) {
        String kodeBarang = "ALAT001";
        if (barang.getTipeBarang() == 1) {
            kodeBarang = "ALAT";
        } else if (barang.getTipeBarang() == 2) {
            kodeBarang = "MTRL";
        }

        String nextNumericId = barangDb.findNextNumericIdByType(kodeBarang);

        if (nextNumericId == null) {
            kodeBarang += "001";
        } else {
            int numericId = Integer.parseInt(nextNumericId.substring(4, 7)) + 1;
            if (numericId < 10) {
                kodeBarang = kodeBarang + "00" + numericId;
            } else if (numericId < 100) {
                kodeBarang = kodeBarang + "0" + numericId;
            } else {
                kodeBarang += numericId;
            }
        }

        barang.setKodeBarang(kodeBarang);
        barangDb.save(barang);

        LogBarang logBarang = new LogBarang();
        logBarang.setBarang(barang);
        logBarang.setAction("Tambah");
        logBarang.setDeskripsi("Tambah barang");
        logBarang.setTanggalWaktu(LocalDateTime.now());
        logBarang.setChangedBy(userService.getCurrentUserName());
        logBarang.setOldValue(barang.getStokBarang());
        barang.setLogBarang(new ArrayList<LogBarang>());
        barang.getLogBarang().add(logBarang);

        logBarangDb.save(logBarang);
    }

    @Override
    public List<Barang> getAllBarang() {
        return barangDb.findAll();
    }

    @Override
    public Barang getBarangById(String kodeBarang) {
        for (Barang barang : getAllBarang()) {
            if (barang.getKodeBarang().equals(kodeBarang)) {
                return barang;
            }
        }
        return null;
    }

    @Override
    public byte[] processFile(MultipartFile file) throws IOException {
        // check if the file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        // check if the file is an image (you can customize this check based on your
        // requirements)
        if (!isImage(file)) {
            throw new IllegalArgumentException("File is not an image");
        }
        // convert MultipartFile to byte[]
        return file.getBytes();
    }

    private boolean isImage(MultipartFile file) {
        // Implement the Logic to check if the file is an image
        // You can use Libraries Like Apache Tika or simply check the file extension
        // For simplicity, let's assume any file with ".jpg", "jpeg", or ".png"
        // extension is considered an image
        String fileName = file.getOriginalFilename();
        return fileName != null
                && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }

    @Override
    public boolean isBarangExistsAndNotDeleted(String namaBarang) {
        // Menggunakan repository atau method lainnya untuk memeriksa apakah barang ada
        // dan isDeleted-nya false
        return barangDb.existsByNamaBarangAndIsDeleted(namaBarang, false);
    }

    @Override
    public void softDeleteBarang(String kodeBarang) {
        Barang barang = barangDb.findById(kodeBarang).orElse(null);

        LogBarang logBarang = createLogBarang(barang, "Hapus", "Hapus barang");

        if (barang != null) {
            barang.setIsDeleted(true);
            barangDb.save(barang);
        }
    }

    @Override
    public Barang updateBarang(Barang barangFromDto) {
        
        Barang barang = getBarangById(barangFromDto.getKodeBarang());

        LogBarang logBarang = createLogBarang(barang, "Ubah", "Ubah barang");
        logBarang.setOldValue(barang.getStokBarang());

        if (barang != null) {

            barang.setBeratBarang(barangFromDto.getBeratBarang());
            barang.setDimensiBarang(barangFromDto.getDimensiBarang());
            barang.setStokBarang(barangFromDto.getStokBarang());
            barang.setStandarStokBarang(barangFromDto.getStandarStokBarang());
            barangDb.save(barang);
        }

        logBarang.setNewValue(barang.getStokBarang());

        logBarangDb.save(logBarang);


        return barang;
    }

    @Override
    public List<Barang> getAllBarangByType(Integer tipeBarang) {
        if (tipeBarang == null) {
            return barangDb.findAll();
        } else {
            return barangDb.findByTipeBarang(tipeBarang);
        }
    }
    public List<LogBarang> getAllLogBarang() {
        return logBarangDb.findAll();
    }

    @Override
    public LogBarang createLogBarang(Barang barang, String action, String deskripsi) {
        LogBarang logBarang = new LogBarang();
        logBarang.setBarang(barang);
        logBarang.setChangedBy(userService.getCurrentUserName());
        logBarang.setAction(action);
        logBarang.setDeskripsi(deskripsi);
        logBarang.setTanggalWaktu(LocalDateTime.now());
        barang.getLogBarang().add(logBarang);
        logBarangDb.save(logBarang);

        return logBarang;
    }

    public List<Barang> searchBarangByName(String name) {
        return barangDb.findByNamaBarangContainingIgnoreCase(name);
    }

    @Override
    public List<Barang> getAllBarangNotDeleted() {
        List<Barang> listBarangAvail = new ArrayList<>();
        for (Barang barang : getAllBarang()) {
            if (!barang.getIsDeleted()) {
                listBarangAvail.add(barang);
            }
        }
        return listBarangAvail;
    }

}