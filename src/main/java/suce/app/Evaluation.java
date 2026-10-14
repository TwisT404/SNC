package suce.app;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluation", nullable = false)
    private Integer id;

    @Column(name = "note", nullable = false)
    private Byte note;

    @Lob
    @Column(name = "commentaire")
    private String commentaire;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_evaluation")
    private Instant dateEvaluation;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_incident", nullable = false)
    private Incident idIncident;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getNote() {
        return note;
    }

    public void setNote(Byte note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Instant getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Instant dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public Incident getIdIncident() {
        return idIncident;
    }

    public void setIdIncident(Incident idIncident) {
        this.idIncident = idIncident;
    }

}