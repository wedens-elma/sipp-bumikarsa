package propensi.c06.sipp.service;

import propensi.c06.sipp.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllList();
    Role getRoleByRoleName(String name);
}