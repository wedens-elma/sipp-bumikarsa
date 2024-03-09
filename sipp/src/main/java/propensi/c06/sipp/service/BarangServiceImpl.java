//package propensi.c06.sipp.service;
//
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import propensi.c06.sipp.model.PengadaanBarang;
//import propensi.c06.sipp.repository.PengadaanBarangDb;
//
//@Service
//@Transactional
//public class PengadaanBarangServiceImpl implements PengadaanBarangService {
//
//    @Autowired
//    PengadaanBarangDb pengadaanBarangDb;
//
//    @Override
//    public void addPengadaanBarang(PengadaanBarang pengadaanBarang){
//        pengadaanBarangDb.save(pengadaanBarang);
//    }
//}
package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.repository.BarangDb;
import java.util.List;

@Service
public class BarangServiceImpl implements BarangService {
    @Autowired
    BarangDb barangDb;

    @Override
    public List<Barang> getAllBarang() { return barangDb.findAll(); }
}
