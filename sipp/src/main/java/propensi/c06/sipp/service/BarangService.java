package propensi.c06.sipp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.model.Barang;

public interface BarangService {
    public void addBarang(Barang barang);

    public List<Barang> getAllBarang();

    Barang getBarangById(String sku);

    public byte[] processFile(MultipartFile file) throws IOException;

    public boolean isBarangExistsAndNotDeleted(String namaBarang);

    void softDeleteBarang(String kodeBarang);

    Barang updateBarang(Barang barang);
}
