package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.VendorDb;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    VendorDb vendorDb;

    @Override
    public List<Vendor> getAllVendor() { return vendorDb.findAll(); }
}

