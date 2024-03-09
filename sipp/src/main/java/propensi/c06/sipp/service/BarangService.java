//package propensi.c06.sipp.service;
//
//import propensi.c06.sipp.model.PengadaanBarang;
//
//public interface PengadaanBarangService {
//    void addPengadaanBarang(PengadaanBarang pengadaanBarang);
//
//}

package propensi.c06.sipp.service;

import java.util.List;
import propensi.c06.sipp.model.Barang;

public interface BarangService {
    List<Barang> getAllBarang();
}
