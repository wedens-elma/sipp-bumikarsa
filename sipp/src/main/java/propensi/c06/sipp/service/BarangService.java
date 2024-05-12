package propensi.c06.sipp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.LogBarang;

public interface BarangService {
    public void addBarang(Barang barang);

    public List<Barang> getAllBarang();

    Barang getBarangById(String sku);

    public byte[] processFile(MultipartFile file) throws IOException;

    public boolean isBarangExistsAndNotDeleted(String namaBarang);

    void softDeleteBarang(String kodeBarang);

    Barang updateBarang(Barang barang);

    List<Barang> getAllBarangByType(Integer tipeBarang);
    List<LogBarang> getAllLogBarang();

    LogBarang createLogBarang(Barang barang, String action, String deskripsi);
    List<Barang> searchBarangByName(String name);

    List<Barang> getAllBarangNotDeleted();
}
