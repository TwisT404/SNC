package snc.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snc.app.Model.Prestataire;

public interface PrestataireRepository extends JpaRepository<Prestataire, Integer> {
}
