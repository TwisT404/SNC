# Journal - SNC (ALTN? 2026-27)
**Groupe :** Molawka Leonardo, Tanghe Cédric

## ReadMe :
- Prérequis : Java, Maven, base;
- Initialiser la base en local : 'db/createBase.sql' et 'db/createData.sql'(Une pour créer les tables et l'autre pour l'alimenter)
- Initialiser la base hébergée : les informations sont dans application.properties pour configurer
- Lancer : "mvn spring-boot:run"
- Application déployée : [https://](https://snc-8biw.onrender.com)
- Comptes de test :
- Limites connues :


## ARCHITECTURE
- Dépôt : [https://…](https://github.com/TwisT404/SNC)
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

- Exceptions : choix de `@ControllerAdvice` pour centraliser la traduction des exceptions métier en codes HTTP. Les ressources absentes renvoient 404, les données invalides 400, et les conflits ou transitions interdites 409. Les erreurs inattendues sont journalisées sans exposer de détails techniques à l'utilisateur.
- `@Controller` et `@RestController` : la plupart des contrôleurs renvoient des vues Thymeleaf, donc utilisent `@Controller`. `AuthentificationController` utilise `@RestController`, car `/api/auth/connexion` renvoie une réponse JSON contenant le JWT, et non une page.
- SOLID : principe(s) appliqué(s) (où, pourquoi) ; principes écartés (pourquoi)
  -  Inversion des dépendances
    - Facilite l'appel des fonctions à la DB -> Plus simple pour les tests unitaires
    - Les controllers ne dépendent pas des services et inversement
    - Swagger : Les controllers peuvent être exécutés plus simplement
- Requête SQL native :
  - Aucune requête SQL native
- @Transactional : où ? pourquoi ?
  - @Transactional


## LOT ALPHA
### Fonctionnalité transverse 1 : …
- Besoin : Journal métier
- Périmètre envisagé : Incidents
- Choix et essais : Ajout, Modifications des incidents par les utilisateurs
- Ce qui fonctionne : AJout, Modifications
- Difficultés : Récupérer l'id de l'utilisateur dans le token
- Reste à faire : L'implémenter dans les autres controllers
  
### Fonctionnalité transverse 2 : …
- Besoin : Supervision avancée
- Périmètre envisagé : Incidents
- Choix et essais :
  - Filtre des incidents du plus ancien au plus récent avec tri par indicateur de couleur
- Ce qui fonctionne :
  - L'ensemble marche  
- Difficultés :
  - Filtrer par indicateurs 
- Reste à faire : Finis
  
## BILAN
- Contribution de chacun :
  - Léonardo :
    - Travaillé sur la partie Login, la gestion du Token et des rôles
    - S'occupe de l'affichage des évaluations pour chaque incident
    - Fais en sorte que le code ait bien été structuré
  - Cédric :
    - Mise en place des fonctionnalités pour les incidents, évaluations sur les incidents et les équipements
    - Mise en place d'une gestion des utilisateurs pour les admins
    - Mise en place du CSS
- Plus grande difficulté et comment elle a été surmontée :
    - Cédric : Mise en place de l'évaluation suite à une résolution -> Proposer 3 options : Résoudre avec évaluation, résoudre sans évaluation et retour sans résolution
    - Léonardo : Mettre en place l'authentification  JWT et de faire fonctionner avec les rôles ->Créer un token à la connexion puis protéger les bonnes pages et actions selon le profil
      
- Fonctionnalités non réalisées et pourquoi : 
  - Les suppresions : manque de temps

  
- Trois points à retenir du cours et du projet : …
  - L'importance des gestions d'erreur
  - L'importance de bien connaître les différences entre chaque annotation
  - L'importance d'avoir une bonne structure ayant chacun un rôle à respecter
