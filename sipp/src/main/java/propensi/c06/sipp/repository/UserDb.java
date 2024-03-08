<<<<<<< HEAD
package propensi.c06.sipp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import propensi.c06.sipp.model.UserModel;

@Repository
public interface UserDb extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}

=======
package propensi.c06.sipp.repository;

import propensi.c06.sipp.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDb extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
>>>>>>> 82c37129d3d78e5516c67c9795ac9eed358e3bee
