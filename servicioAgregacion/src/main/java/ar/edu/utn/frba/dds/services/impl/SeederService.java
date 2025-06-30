package ar.edu.utn.frba.dds.services.impl;


import ar.edu.utn.frba.dds.services.ISeederService;
import org.springframework.stereotype.Service;

@Service
public class SeederService implements ISeederService {

    private String baseurl = "http://localhost:8080";

    @Override
    public void init() {

    }
}
