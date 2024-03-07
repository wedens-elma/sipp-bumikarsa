package propensi.c06.sipp.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.repository.BarangDb;


@Service
public class BarangServiceImpl implements BarangService {

    @Autowired
    BarangDb barangDb;


    @Override
    public void addBarang(Barang barang) {
        String kodeBarang = "ALAT001";
        // System.out.println(barang.getNamaBarang());
        // if (barang.getTipeBarang() == 1) {
        //     kodeBarang = "ALAT";
        // } else if (barang.getTipeBarang() == 2) {
        //     kodeBarang = "MTRL";
        // }

        // String nextNumericId = barangDb.findNextNumericIdByType(kodeBarang);

        // if (nextNumericId == null) {
        //     kodeBarang += "001";
        // } else {
        //     int numericId = Integer.parseInt(nextNumericId.substring(4, 7)) + 1;
        //     if (numericId < 10) {
        //         kodeBarang = kodeBarang + "00" + numericId;
        //     } else if (numericId < 100) {
        //         kodeBarang = kodeBarang + "0" + numericId;
        //     } else {
        //         kodeBarang += numericId;
        //     }
        // }

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

}
