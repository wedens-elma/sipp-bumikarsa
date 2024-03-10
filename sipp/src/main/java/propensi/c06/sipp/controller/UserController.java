package propensi.c06.sipp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.dto.UserMapper;
import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.UserDb;
import propensi.c06.sipp.service.RoleService;
import propensi.c06.sipp.service.UserService;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserDb userDb;

    @GetMapping("/user/add")
    private String addUserForm(Model model){
        UserModel user = new UserModel();
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO();
        model.addAttribute("userDTO", createUserRequestDTO);
        model.addAttribute("listRole", roleService.getAllList());
        model.addAttribute("user", user);
        return "form-create-user";
    }

    @PostMapping("/user/add")
    private String addUserSubmit(@ModelAttribute CreateUserRequestDTO createUserRequestDTO, Model model){
        UserModel userModel = new UserModel();
        // userMapper.createUserRequestDTOToUserModel(createUserRequestDTO);
        var role = roleService.getRoleByRoleName(createUserRequestDTO.getRole());

        if (userDb.findByNameAndRole(createUserRequestDTO.getName(), role).isPresent()) {
            return "double-account";
        }

        // userModel.setDeleted(false);
        userModel.setEmail(createUserRequestDTO.getEmail());
        userModel.setName(createUserRequestDTO.getName());
        userModel.setPassword(createUserRequestDTO.getPassword());
        userModel.setRole(role);

        // if (!file.isEmpty()) {
        //     byte[] imageContent;
        //     try {
        //             imageContent = userService.processFile(file);
        //             userModel.setImage(imageContent);
        //     } catch (IOException e) {
        //         model.addAttribute("error", "Error processing the file");
        //         model.addAttribute("userDTO", createUserRequestDTO);
        //         model.addAttribute("listRole", roleService.getAllList());
        //         model.addAttribute("user", userModel);
        //         return "form-create-user";
        //     }
        // }
        // try {
        //     user.setImage(createUserRequestDTO.getFile().getBytes());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        userService.addUser(userModel, createUserRequestDTO);
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String viewAllUsers(Model model) {
        List<UserModel> users = userService.getActiveUsers();
        model.addAttribute("users", users);
        return "viewall-user";
    }

    @GetMapping("/user/soft-delete/{email}")
    private String softDeleteUser(@PathVariable String email, Model model) {
        UserModel userModel = userService.getUserByEmail(email);
        model.addAttribute("email", userModel.getEmail());
        // userService.softDeleteUser(email);
        return "confirm-delete-user";
    }

    @GetMapping("/user/soft-delete/{email}/confirm")
    private String confirmDeleteUser(@PathVariable String email, Model model) {
        userService.softDeleteUser(email);
        return "redirect:/user";
    }

}
