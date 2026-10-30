package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Equipement;
import snc.app.Repository.EquipementRepository;
import snc.app.Repository.StationRepository;

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
}
