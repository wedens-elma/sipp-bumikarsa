package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.c06.sipp.model.LogRencana;

@Repository
public interface LogRencanaDb extends JpaRepository<LogRencana, Long> {
    
}
