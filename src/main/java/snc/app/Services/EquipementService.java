package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Exception.ConflitDonneesException;
import snc.app.Exception.DonneeInvalideException;
import snc.app.Exception.RessourceIntrouvableException;
import snc.app.Model.Equipement;
import snc.app.Model.Station;
import snc.app.Repository.EquipementRepository;
import snc.app.Repository.StationRepository;

import java.util.List;

@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;
    private final StationRepository stationRepository;

    public EquipementService(EquipementRepository equipementRepository, StationRepository stationRepository) {
        this.equipementRepository = equipementRepository;
        this.stationRepository = stationRepository;
    }

    public List<Equipement> afficherEquipements() {
        return equipementRepository.findAll();
    }

    public Equipement afficherEquipement(int id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("L'équipement " + id + " n'existe pas."));
    }

    public void ajouterEquipement(String referenceUnique, String libelle, String type,
                                  String etatFonctionnement, Integer stationId) {
        if (referenceUnique == null || referenceUnique.isBlank()
                || libelle == null || libelle.isBlank()
                || type == null || type.isBlank()
                || etatFonctionnement == null || etatFonctionnement.isBlank()
                || stationId == null) {
            throw new DonneeInvalideException("Tous les champs sont obligatoires.");
        }

        String referenceNormalisee = referenceUnique.trim();
        if (equipementRepository.existsByReferenceUnique(referenceNormalisee)) {
            throw new ConflitDonneesException("Cette référence d'équipement existe déjà.");
        }

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RessourceIntrouvableException("La station sélectionnée n'existe pas."));

        Equipement equipement = new Equipement();
        equipement.setReferenceUnique(referenceNormalisee);
        equipement.setLibelle(libelle.trim());
        equipement.setType(type.trim());
        equipement.setEtatFonctionnement(etatFonctionnement.trim());
        equipement.setIdStation(station);
        equipementRepository.save(equipement);
    }
}
