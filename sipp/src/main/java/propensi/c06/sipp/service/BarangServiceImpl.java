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
