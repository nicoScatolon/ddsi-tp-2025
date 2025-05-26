package ar.edu.utn.frba.dds.domain.entities.DetectorSpam.impl;

import ar.edu.utn.frba.dds.domain.entities.DetectorSpam.IDetectorDeSpam;
import org.springframework.stereotype.Service;

@Service
public class DetectorDeSpam implements IDetectorDeSpam {
    @Override
    public boolean esSpam(String texto) {
        return false;
    }
}
