package propensi.c06.sipp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.repository.RencanaDb;

@Service
public class RencanaServiceImpl implements RencanaService {
    @Autowired
    RencanaDb rencanaDb;

    @Override
    public List<Rencana> getAllRencana() { return rencanaDb.findAll(); }
}
