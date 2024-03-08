<<<<<<< HEAD
package propensi.c06.sipp.repository;

import propensi.c06.sipp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDb extends JpaRepository<Role, Long> {
    @SuppressWarnings("null")
    List<Role> findAll();
    Optional<Role> findByRole(String role);
}
=======
package propensi.c06.sipp.repository;

import propensi.c06.sipp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDb extends JpaRepository<Role, Long> {
    @SuppressWarnings("null")
    List<Role> findAll();
    Optional<Role> findByRole(String role);
}
>>>>>>> 82c37129d3d78e5516c67c9795ac9eed358e3bee
