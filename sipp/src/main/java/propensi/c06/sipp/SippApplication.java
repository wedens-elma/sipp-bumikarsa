package propensi.c06.sipp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.BarangMapper;
import propensi.c06.sipp.dto.UserMapper;
import propensi.c06.sipp.dto.VendorMapper;
import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.model.Role;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.RoleDb;
import propensi.c06.sipp.service.BarangService;
import propensi.c06.sipp.service.PengadaanService;
import propensi.c06.sipp.service.RencanaService;
import propensi.c06.sipp.service.RoleService;
import propensi.c06.sipp.service.UserService;
import propensi.c06.sipp.service.VendorService;

@SpringBootApplication
public class SippApplication {

	public static void main(String[] args) {
		SpringApplication.run(SippApplication.class, args);
	}

	// CommandLineRunner digunakan untuk execute code saat spring pertama kali start up
	@Bean
	@Transactional
	CommandLineRunner run(UserService userService, RoleService roleService, RoleDb roleDb, UserMapper userMapper, RencanaService rencanaService, BarangService barangService, VendorService vendorService, PengadaanService pengadaanService, BarangMapper barangMapper, VendorMapper vendorMapper){
		return args -> {

			if (roleService.getAllList().isEmpty()){

				Role roleAdmin = new Role();
				roleAdmin.setRole("Admin");
				roleDb.save(roleAdmin);

				Role roleManajer = new Role();
				roleManajer.setRole("Manajer");
				roleDb.save(roleManajer);

				Role roleOperasional = new Role();
				roleOperasional.setRole("Operasional");
				roleDb.save(roleOperasional);

				Role roleKeuangan = new Role();
				roleKeuangan.setRole("Keuangan");
				roleDb.save(roleKeuangan);

			}

			if (userService.getActiveUsers().isEmpty()){
				var admin = new CreateUserRequestDTO();
				admin.setEmail("admin1@gmail.com");
				admin.setName("Admin 1");
				admin.setPassword("AdminPropensi");
				admin.setRole("Admin");
				UserModel userAdmin = userMapper.createUserRequestDTOToUserModel(admin);
				userService.addUser(userAdmin, admin);

				var manajer = new CreateUserRequestDTO();
				manajer.setEmail("manajer1@gmail.com");
				manajer.setName("Manajer 1");
				manajer.setPassword("ManajerPropensi");
				manajer.setRole("Manajer");
				UserModel userManajer = userMapper.createUserRequestDTOToUserModel(manajer);
				userService.addUser(userManajer, manajer);

				var operasional = new CreateUserRequestDTO();
				operasional.setEmail("operasional1@gmail.com");
				operasional.setName("Operasional 1");
				operasional.setPassword("OperasionalPropensi");
				operasional.setRole("Operasional");
				UserModel userOperasional = userMapper.createUserRequestDTOToUserModel(operasional);
				userService.addUser(userOperasional, operasional);

				var keuangan = new CreateUserRequestDTO();
				keuangan.setEmail("keuangan1@gmail.com");
				keuangan.setName("Keuangan 1");
				keuangan.setPassword("KeuanganPropensi");
				keuangan.setRole("Keuangan");
				UserModel userKeuangan = userMapper.createUserRequestDTOToUserModel(keuangan);
				userService.addUser(userKeuangan, keuangan);

			}

			// if (barangService.getAllBarang().isEmpty()) {
			// 	var barang = new CreateTambahBarangRequestDTO();
			// 	barang.setNamaBarang("Barang 1");
			// 	barang.setDeskripsiBarang("Ini adalah Barang 1");
			// 	barang.setTipeBarang(1);
			// 	barang.setStokBarang(50);
			// 	barang.setStandarStokBarang(5);
			// 	Barang barangSave = barangMapper.createTambahBarangRequestDTO(barang);
			// 	barangService.addBarang(barangSave);
			// }

			// if (vendorService.getAllVendors().isEmpty()) {
			// 	var vendor = new CreateVendorRequestDTO();
			// 	vendor.setEmailVendor("vendor1@gmail.com");
			// 	vendor.setNamaVendor("Vendor Pertama");
			// 	vendor.setNomorHandphoneVendor("081234567890");
			// 	vendorService.addVendor(vendor);
			// }

		};

	}

}
