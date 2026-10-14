package suce.app.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur", nullable = false)
    private Integer id;

    @Column(name = "identifiant_connexion", nullable = false, length = 50)
    private String identifiantConnexion;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "profil", nullable = false, length = 50)
    private String profil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentifiantConnexion() {
        return identifiantConnexion;
    }

    public void setIdentifiantConnexion(String identifiantConnexion) {
        this.identifiantConnexion = identifiantConnexion;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

}