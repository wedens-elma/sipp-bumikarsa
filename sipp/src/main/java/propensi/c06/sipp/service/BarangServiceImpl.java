package propensi.c06.sipp.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.repository.BarangDb;

@Service
public class BarangServiceImpl implements BarangService {

    @Autowired
    BarangDb barangDb;

    @Override
    public void addBarang(Barang barang) {
        String kodeBarang = "ALAT001";
        System.out.println(barang.getNamaBarang());
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
    public byte[] processFile(MultipartFile file) throws IOException{
    // check if the file is not empty
    if (file.isEmpty()) {
    throw new IllegalArgumentException("File is empty");
    }
    // check if the file is an image (you can customize this check based on your requirements)
    if (!isImage(file)) {
    throw new IllegalArgumentException("File is not an image");
    }
    // convert MultipartFile to byte[]
    return file.getBytes();
    }

    private boolean isImage(MultipartFile file) {
    // Implement the Logic to check if the file is an image
    // You can use Libraries Like Apache Tika or simply check the file extension
    // For simplicity, let's assume any file with ".jpg", "jpeg", or ".png" extension is considered an image
    String fileName = file.getOriginalFilename();
    return fileName !=null && (fileName.endsWith(".jpg")|| fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }
}