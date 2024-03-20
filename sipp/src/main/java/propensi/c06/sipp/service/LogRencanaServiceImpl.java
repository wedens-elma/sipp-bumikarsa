package propensi.c06.sipp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.c06.sipp.model.LogRencana;
import propensi.c06.sipp.repository.LogRencanaDb;

@Service
public class LogRencanaServiceImpl implements LogRencanaService {
    @Autowired
    LogRencanaDb logRencanaDb;

    @Override
    public List<LogRencana> getAllLogRencana() { return logRencanaDb.findAll(); }
}
