package ar.edu.utn.frba.dds.utils.DetectorSpam.impl;

import ar.edu.utn.frba.dds.utils.DetectorSpam.IDetectorDeSpam;
import org.springframework.stereotype.Service;

@Service
public class DetectorDeSpam implements IDetectorDeSpam {
    @Override
    public boolean esSpam(String texto) {
        return false;
    }
}
