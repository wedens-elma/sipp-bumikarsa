package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.dto.request.LoginJwtRequestDTO;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.UserDb;
import propensi.c06.sipp.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDb userDb;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO){
        user.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
        String hashedPass = encrypt(user.getPassword());
        user.setPassword(hashedPass);
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO) {
        String email = loginJwtRequestDTO.getEmail();
        String name = loginJwtRequestDTO.getName();

        UserModel user = userDb.findByEmail(email);

        if(user == null){
            user = new UserModel();
            user.setName(name);
            user.setPassword("bacabaca");
            user.setEmail(email);
            user.setRole(roleService.getRoleByRoleName("Admin"));
            userDb.save(user);
        }


        return jwtUtils.generateJwtToken(loginJwtRequestDTO.getEmail());
    }

    @Override
    public String getCurrentUserRole(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            UserModel userModel = getUserByEmail(user.getUsername());

            System.out.println(userModel.getRole().getRole());

            String role = userModel.getRole().getRole();

            return role;
        }
        return null;
    }

    private UserModel getUserByEmail(String username) {
        return userDb.findByEmail(username);
    }
}