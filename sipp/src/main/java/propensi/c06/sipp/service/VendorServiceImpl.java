package propensi.c06.sipp.service;
import java.util.List;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.response.VendorMapper;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.VendorDb;

@Service
@Transactional
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorDb vendorDb;
    private final VendorMapper vendorMapper;

    @Override
    public void createVendor(CreateVendorRequestDTO createVendorRequestDTO) {
        Vendor vendor = vendorMapper.createVendorRequestDTOToVendor(createVendorRequestDTO);
        vendorDb.save(vendor);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorDb.findAll();
    }

    @Override
    public Vendor getVendorByCode(String kodeVendor) {
        return vendorDb.findById(kodeVendor).orElse(null);
    }

    @Override
    public List<Vendor> getVendorByName(String namaVendor) {
        return vendorDb.findByNamaVendorContaining(namaVendor);
    }
}