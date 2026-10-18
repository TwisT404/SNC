package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Equipement;

public interface EquipementRepository extends JpaRepository<Equipement, Integer> {
}
