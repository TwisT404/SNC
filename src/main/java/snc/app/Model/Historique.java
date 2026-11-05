package snc.app.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "historique")
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "utilisateur_id", nullable = false)
    private Integer utilisateurId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "entite_concerne", length = 100)
    private String entiteConcerne;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_action", nullable = false)
    private Instant dateAction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntiteConcerne() {
        return entiteConcerne;
    }

    public void setEntiteConcerne(String entiteConcerne) {
        this.entiteConcerne = entiteConcerne;
    }

    public Instant getDateAction() {
        return dateAction;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

}