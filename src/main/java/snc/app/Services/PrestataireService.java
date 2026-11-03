package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Prestataire;
import snc.app.Repository.PrestataireRepository;

import java.util.List;

@Service
public class PrestataireService {

    private PrestataireRepository prestataireRepository;

    public PrestataireService(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    public List<Prestataire> afficherPrestataires() {
        return prestataireRepository.findAll();
    }
}
