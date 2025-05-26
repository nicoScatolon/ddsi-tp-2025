package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.services.IDetectorDeSpam;
import org.springframework.stereotype.Service;

@Service
//ToDO: Esta bien que sea un service?
public class DetectorDeSpam implements IDetectorDeSpam {
    @Override
    public boolean esSpam(String texto) {
        return false;
    }
}
