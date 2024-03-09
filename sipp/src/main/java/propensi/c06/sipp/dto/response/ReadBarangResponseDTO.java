package propensi.c06.sipp.dto.response;

import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadBarangResponseDTO {
    private String namaBarang;
    private String deskripsiBarang;
    private Integer tipeBarang;
    private String dimensiBarang;
    private String beratBarang;
    private byte[] image;
    private Integer stokBarang;
    private Integer standarStokBarang;


    public void setImageBase64(String base64){
        this.image = Base64.getDecoder().decode(base64);
    }

    public String getImageBase64(String base64){
        return this.image != null ? Base64.getEncoder().encodeToString(this.image) : null;
    }
    
}
