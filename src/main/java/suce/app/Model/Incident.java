package suce.app.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "incident")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_ouverture")
    private Instant dateOuverture;

    @ColumnDefault("'Ouvert'")
    @Column(name = "statut", nullable = false, length = 50)
    private String statut;

    @Column(name = "date_resolution")
    private Instant dateResolution;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_equipement", nullable = false)
    private Equipement idEquipement;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_prestataire")
    private Prestataire idPrestataire;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(Instant dateResolution) {
        this.dateResolution = dateResolution;
    }

    public Equipement getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(Equipement idEquipement) {
        this.idEquipement = idEquipement;
    }

    public Prestataire getIdPrestataire() {
        return idPrestataire;
    }

    public void setIdPrestataire(Prestataire idPrestataire) {
        this.idPrestataire = idPrestataire;
    }

}