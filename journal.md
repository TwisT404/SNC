# Journal - SNC (ALTN? 2026-27)
**Groupe :** Molawka Leonardo, Tanghe Cédric

## ReadMe :
- Prérequis : Java, Maven, base;
- Initialiser la base en local : 'db/createBase.sql' et 'db/createData.sql'(Une pour créer les tables et l'autre pour l'alimenter)
- Initialiser la base hébergée : les informations sont dans application.properties pour configurer
- Lancer : "mvn spring-boot:run"
- Application déployée : https://
- Comptes de test :
- Limites connues :


## ARCHITECTURE
- Dépôt : https://…
- Schéma : ![architecture](docs/architecture.png)

## TESTS
- Stratégie : …
- Commande : `mvn verify`
- Couvert : … | Non couvert : … (pourquoi)

## Choix Techniques :
- Répartition des droits par profil 

|  N°   |            Fonctionnalité             | Responsable d’exploitation | Chargé d'incidence | 
|:-----:|:-------------------------------------:|:--------------------------:|:------------------:|
| F2–F3 |    Consulter équipements et fiches    |             X              |         X          |
|  F4   |         Déclarer un incident          |             X              |                    |
| F5-F6 |   Consulter / modifier un incident    |             X              |         X          |
|  F7   | Affecter ou réaffecter un prestataire |             X              |                    |
|  F8   |      Déclarer un incident résolu      |             X              |         X          |
|  F9   |        Consulter l’historique         |             X              |         X          |
|  F10  |        Évaluer un prestataire         |             X              |                    |
|  F11  |            Tableau de bord            |             X              |         X          |

- Exceptions : @ControllerAdvice ou ResponseStatusException ? Pourquoi ?
- SOLID : principe(s) appliqué(s) (où, pourquoi) ; principes écartés (pourquoi)
- Requête SQL native : où ? analyse critique
- @Transactional : où ? pourquoi ?

## LOT ALPHA
### Fonctionnalité transverse 1 : …
- Besoin · périmètre envisagé · choix et essais · ce qui fonctionne · difficultés · reste à faire
### Fonctionnalité transverse 2 : …

## BILAN
- Contribution de chacun : …
- Plus grande difficulté et comment elle a été surmontée : …
- Fonctionnalités non réalisées et pourquoi : …
- Trois points à retenir du cours et du projet : …