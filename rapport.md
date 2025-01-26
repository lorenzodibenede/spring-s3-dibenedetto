# Rapport

### Sujet 3 : Points d'intérêt touristiques JEE/Spring

---

## Architecture

L'application doit permettre à des utilisateurs de soumettre des lieux d'intérêt touristique, de pouvoir les consulter 
et de les évaluer. Un lieu soumi peut être valider ou rejeter par un administrateur afin de pouvoir être consulté.

L'architecture de l'application est organisé en couche :
- Couche Controller : Gère les requêtes HTTP entrantes et les réponses, elle délège le traitement au service approprié
- Couche Service : Contient la logique métier. S'occupe de coordonner les actions entre les différentes composants
- Couche Repository : S'occupe de l'accès aux données ainsi que les différentes actions sur celle-ci. elle est employée par la couche Service

```
spring-s3-dibenedetto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ch.hearc.jee2024.tourismapi/
│   │   │       ├── controller/
│   │   │       ├── DTO/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── utils/
│   │   │       └── TourismApiApplication.java
│   │   └── ressources/
│   │       ├── application.properties
│   │       └── application-h2.properties
│   └── test/
│       └── java
│           └── ch.hearc.jee2024.tourismapi
│               └── TourismApiApplicationTests
└── pom.xml
```

Ces différentes couches sont représenté dans la structure de fichier par des dossiers comportant le nom de la couche. 
Nous pouvons également y trouver un dossier `entity` contenant les ressources à manipuler dans l'application. Le dossier
`DTO` de contrôler précisément les données échangées avec le client. Et enfin le dossier utils contenant des classes utilitaies 
ou annexes.

Nous y trouvons également le point d'entrée de l'application se trouve dans la classe `TousismApiApplication`, le fichier de configuration pour Maven `pom.xml`.
et pour terminer, les différents tests du projet ce trouvent dans la classe `TourismApiApplicationTests`.

---

## Implémentation

```
spring-s3-dibenedetto/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── ch.hearc.jee2024.tourismapi/
│   └── ...     ├── controller/
│               │    ├── LocationController.java
│               │    └── UserController.java
│               ├── DTO/
│               │    └── RatingRequestDTO.java
│               ├── entity/
│               │    ├── Location.java
│               │    ├── Rating.java
│               │    └── User.java
│               ├── repository/
│               │    ├── LocationRepository.java
│               │    ├── RatingRepository.java
│               │    └── UserRepository.java
│               ├── service/
│               │    ├── LocationRepository.java
│               │    ├── RatingRepository.java
│               │    └── UserRepository.java
│               ├── utils/
│               │    ├── RatingId.java
│               │    └── Role.java
│               └── TourismApiApplication
├──  ...
└── pom.xml
```

### Entity

Chaque classe se trouvant dans ce dossier représente une entitée de l'application correpondant également à une table de
la base de données. Nous y trouvons les entités suivantes.

#### Location

L'entité `Location` représente un lieu d'intérêt touristique.

Attributs :

- id : Identifiant unique du lieu.
- name : Nom du lieu.
- description : Description détaillée.
- latitude et longitude : Coordonnées GPS.
- averageRating : Note moyenne attribuée au lieu.
- submittedBy : Utilisateur ayant soumis le lieu.
- validatedBy : Administrateur ayant validé ou rejeté le lieu.
- ratings : Liste des évaluations associées au lieu (relation OneToMany avec l'entité Rating).

Cette Entité utilise les annotations `@Entity` et `@Table` qui l'a défini comme un enetité JPA liée à une table `locations`.
L'annotation `@JsonView` y est également employé permettant de contrôler la visibilité des champs dans les réponses JSON.

#### User

L'entité `User` représente un utilisateur ou un administrateur.

Attributs  :

- id : Identifiant unique de l'utilisateur.
- name : Nom de l'utilisateur.
- password : Mot de passe de l'utilisateur.
- role : Rôle de l'utilisateur (exemple : USER, ADMIN).

Cette Entité emploie également les annotations `@Entity`, `@Table` et `@JsonView`. Cependant, une utilisation de l'annotation
`@Enumerated(EnumType.STRING)` permet d'indique que le rôle est stocké sous forme de chaîne dans la base de données.

#### Rating

L'entité `Rating` représente une évaluation d'un utilisateur pour un lieu.

Attributs :

- id : Identifiant composite (clé primaire) qui combine l'ID de l'utilisateur et l'ID du lieu. Il est géré à l'aide de l'objet `RatingId`.
- user : Référence vers l'utilisateur ayant soumis l'évaluation, avec une liaison à la clé userId dans `RatingId`.
- location : Référence vers le lieu touristique évalué, liée à la clé locationId dans `RatingId`.
- rating : Note attribuée au lieu, comprise entre 0 et 5.

`RatingId` est une classe utilitaire représentant une clé composite utilisée pour identifier de manière unique une évaluation 
dans la table ratings.

Attributs :

- userId : Identifiant de l'utilisateur ayant soumis une évaluation.
- locationId : Identifiant du lieu touristique évalué.

Cette classe utilise l'annotation @Embeddable indiquant qu'elle peut être embarqué dans une autre entité. Elle est donc 
embarqué dans `Rating` afin d'être utilisée comme clé composite.

### Controller

Les controllers exposent les endpoints de l'application, vous trouverez les détails des endpoints de l'API REST dans le 
fichier `README.md` du projet.

#### LocationController

Ce controller permet la gestion des lieux touristiques, voici les différentes méthodes implémentées :
- submitLocation : Permet à un utilisateur de soumettre un nouveau lieu touristique.
- getLocations : Récupère une liste paginée de lieux validés ou tous les lieux si l'utilisateur est administrateur.
- getLocationById : Renvoie les détails d’un lieu spécifique.
- rateLocation : Permet à un utilisateur d'ajouter ou de mettre à jour une évaluation pour un lieu.
- getRatingsByLocation : Renvoie toutes les évaluations associées à un lieu spécifique.
- validateLocation : Permet à un administrateur de valider un lieu soumis.
- rejectLocation : Permet à un administrateur de rejeter un lieu soumis.

#### UserController
Ce controller permet la gestion des utilisateurs, voici les différentes méthodes implémentées :
- registerUser : Permet de créer un nouvel utilisateur avec un rôle défini.
- getUser : Récupère les informations d'un utilisateur spécifique en excluant son mot de passe.
- getAllUsers : Renvoie la liste de tous les utilisateurs enregistrés.
- getAllAdmins : Renvoie la liste de tous les utilisateurs ayant le rôle d'administrateur.

### DTO

Cette partie contient une classe de transfert de données (Data Transfer Object), la classe `RatingRequestDTO` est utilisée 
pour encapsuler les données nécessaires lorsqu'un utilisateur soumet une évaluation pour une destination touristique.


### Repository

Cette couche contient les classes `LocationRepository`, `UserRepository` et la classe `UserRepository` fournissant des méthodes
pour accéder et ajouter des données dans les différentes tables de la base de données H2, qui est une basse de données SQL légère 
en mémoire intégré la rendant temporaire :

- Table locations :
  - id
  - average_rating
  - description
  - latitude
  - longitude
  - name
  - submitted_by
  - validated_by
- Table users :
  - id
  - name
  - password
  - role
- Table rating
  - location_id
  - user_id
  - rating


### Service

Cette couche contient la logique métier et agit comme une passerelle entre la couche Controller et Repository.

#### LocationService
Ce service gère la logique métier pour les lieux touristiques, voici les différentes méthodes implémentées :
- submitLocation : Permet à un utilisateur de soumettre un nouveau lieu. Si l'utilisateur est un administrateur, le lieu est validé automatiquement.
- getLocationById : Récupère un lieu par son identifiant.
- getAllLocations : Récupère tous les lieux, avec possibilité de pagination.
- getValidatedLocations : Récupère uniquement les lieux validés, avec pagination.
- validateLocation : Permet à un administrateur de valider un lieu soumis.
- rejectLocation : Supprime un lieu soumis.
- paginateLocation : Gère la découpe des résultats en fonction de la page et de la taille de page spécifiées.


#### UserService
Ce service s'occupe de la logique métier pour les utilisateurs et administrateurs de l'application :
- createUser : Crée un nouvel utilisateur avec un nom, un mot de passe et un rôle (par défaut, le rôle est USER). La méthode vérifie que le nom et le mot de passe ne sont pas vides avant de créer l'utilisateur.
- getUserById : Récupère un utilisateur par son identifiant.
- getAllUsers : Récupère tous les utilisateurs de la base de données.
- getAllAdmins : Filtre et retourne uniquement les utilisateurs ayant le rôle ADMIN.

#### RatingService
Ce service gère la logique métier liées aux évaluations des lieux :
- addOrUpdateRating : Permet à un utilisateur d'ajouter ou de mettre à jour une évaluation pour un lieu donné.
- getRatingsByLocation : Récupère toutes les évaluations associées à un lieu donné.
- getRatingsByUser : Récupère toutes les évaluations faites par un utilisateur spécifique.

---

## Planning initial vs effectif

Chaque ligne représente le travail fourni la semaine, la date indiquée est le mercredi de cette semaine.

| Planning | Initial                                                                   | Effectif                                                                                                                  |
|----------|---------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| 13.11.24 | Générer des idées                                                         | Générer des idées                                                                                                         |
| 20.11.24 | Création du projet et des entités                                         | Création du projet et des entités                                                                                         |
| 27.11.24 | Création des endpoints de soumission et consultation des lieux            | Création des endpoints de soumission et consultation des lieux                                                            |
| 04.12.24 | Ajout de la couche Service et de la couche Repository                     | Ajout de la couche Service et de la couche Repository                                                                     |
| 11.12.24 | Implémentation de tests unitaires                                         | Création de l'enregistrement utilisateur et consultation des utilisateurs                                                 |
| 18.12.24 | Configuration du Repository avec DB                                       | Configuration du Repository avec DB                                                                                       |
| 25.12.24 | Création de l'enregistrement utilisateur et consultation des utilisateurs | Décider de prendre des vacances                                                                                           |
| 01.01.25 | Ajout de la possibilité pour les utilisateurs de noter un lieu            | Décider de prendre des vacances                                                                                           |
| 08.01.25 | Permettre à l'administrateur de valider ou de rejeter des lieux           | Rédaction du rapport                                                                                                      |
| 15.01.25 | Implémentations de tests unitaires                                        | Ajout de la possibilité pour les utilisateurs de noter un lieu + Rédaction du rapport                                     |
| 22.01.25 | Rédaction du rapport                                                      | Permettre à l'administrateur de valider ou de rejeter des lieux + implémentaion de tests unitaires + Rédaction du rapport |
| 26.01.25 | Rendu                                                                     | Rendu                                                                                                                     |

---

## Bilan

Le projet pose des bases solides pour une plateforme de gestion des points d’intérêt touristiques. Les 
fonctionnalités principales ont été mises en place avec une architecture en couches, favorisant la clarté et la 
maintenabilité du code.

Cependant, certaines améliorations peuvent être envisagées pour renforcer la fiabilité et l’expérience utilisateur. Par 
exemple, la gestion des erreurs d’entrée utilisateur pourrait être perfectionnée, avec des validations plus complètes pour 
assurer une meilleure qualité des données saisies. Du côté des tests, une couverture plus étendue permettrait de prévenir 
d’éventuelles anomalies. En termes de sécurité et de gestion des accès, des améliorations sont également possibles. En effet 
La protection des mots de passe n’a pas été intégrée, et l’implémentation d’un système d’authentification plus 
avancé, comme l’utilisation de JWT, pourrait renforcer la gestion des rôles et des autorisations de l'application.

En conclusion, le projet remplit ses objectifs initiaux, avec des points à améliorer concernant l'optimisation, la 
sécurité et la robustesse du projet. 

---

## Bibliographie

**3.7.3 Composite identifiers with @EmbeddedId**, *Hibernate ORM User Guide* :
https://docs.jboss.org/hibernate/orm/6.3/userguide/html_single/Hibernate_User_Guide.html#identifiers-composite-aggregated

**Jackson JSON**, *Spring Docs* :
https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/jackson.html

**Mapping Requests**, Spring Docs* :
https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html


