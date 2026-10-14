package snc.app.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipement")
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipement", nullable = false)
    private Integer id;

    @Column(name = "reference_unique", nullable = false, length = 100)
    private String referenceUnique;

    @Lob
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "etat_fonctionnement", nullable = false, length = 50)
    private String etatFonctionnement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_station", nullable = false)
    private Station idStation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceUnique() {
        return referenceUnique;
    }

    public void setReferenceUnique(String referenceUnique) {
        this.referenceUnique = referenceUnique;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtatFonctionnement() {
        return etatFonctionnement;
    }

    public void setEtatFonctionnement(String etatFonctionnement) {
        this.etatFonctionnement = etatFonctionnement;
    }

    public Station getIdStation() {
        return idStation;
    }

    public void setIdStation(Station idStation) {
        this.idStation = idStation;
    }

}