package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Exception.RessourceIntrouvableException;
import snc.app.Model.Equipement;
import snc.app.Repository.EquipementRepository;

import java.util.List;

@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;

    public EquipementService(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    public List<Equipement> afficherEquipements() {
        return equipementRepository.findAll();
    }

    public Equipement afficherEquipement(int id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("L'équipement " + id + " n'existe pas."));
    }
}
