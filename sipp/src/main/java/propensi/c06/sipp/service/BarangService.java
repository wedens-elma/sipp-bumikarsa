package propensi.c06.sipp.service;

import java.util.List;

import propensi.c06.sipp.model.Barang;

public interface BarangService {
    public void addBarang(Barang barang);

    public List<Barang> getAllBarang();

    Barang getBarangById(String sku);

}
