package propensi.c06.sipp.service;

import java.util.List;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.model.Rencana;

public interface LogRencanaService {
    List<LogRencana> getAllLogRencana();

    void createLogRencana(Rencana rencana, String status, String feedback);
}
