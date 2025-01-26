# Documentation de l'API

*Développé par Lorenzo Di Benedetto pour le cours de JEE-Spring*

---

## **POST** `/api/users/register`
### Enregistrer un utilisateur

Permet de créer un nouvel utilisateur dans le système.

#### Paramètres :
- **Body :**
    - JSON représentant un utilisateur contenant :
        - `name` (String) : Nom de l'utilisateur.
        - `password` (String) : Mot de passe de l'utilisateur.
        - `role` (String) : Rôle de l'utilisateur (`USER` ou `ADMIN`).

#### Réponses :
- **200 OK** : L'utilisateur créé avec ses informations.
- **400 Bad Request** : Si les données sont invalides (ex. rôle non valide ou données manquantes).

---

## **GET** `/api/users/{id}`
### Récupérer les détails d'un utilisateur

Renvoie les informations d'un utilisateur par son ID, sans inclure le mot de passe.

#### Paramètres :
- **Path Parameters :**
    - `id` (Long) : Identifiant de l'utilisateur.

#### Réponses :
- **200 OK** : Informations de l'utilisateur.
- **404 Not Found** : Si l'utilisateur n'existe pas.

---

## **GET** `/api/users`
### Récupérer tous les utilisateurs

Renvoie une liste de tous les utilisateurs présents dans le système.

#### Réponses :
- **200 OK** : Liste des utilisateurs, chaque utilisateur comprenant les informations de base (nom, rôle).

---

## **GET** `/api/users/admins`
### Récupérer tous les administrateurs

Renvoie une liste de tous les utilisateurs ayant le rôle d'administrateur.

#### Réponses :
- **200 OK** : Liste des administrateurs, chaque administrateur comprenant les informations pertinentes.

---

## **POST** `/api/locations`
### Soumettre un nouveau lieu

Crée un lieu et le soumet pour validation.

#### Paramètres :
- **Query Parameters :**
    - `userId` (Long) : Identifiant de l'utilisateur soumettant le lieu.
- **Body :**
    - JSON représentant un lieu contenant :
        - `name` (String) : Nom du lieu.
        - `description` (String) : Description du lieu.
        - `latitude` (Double) : Latitude géographique.
        - `longitude` (Double) : Longitude géographique.

#### Réponses :
- **200 OK** : Le lieu soumis.
- **400 Bad Request** : Si les données sont invalides.

---

## **GET** `/api/locations`
### Récupérer des lieux

Retourne une liste de lieux validés ou tous les lieux si l'utilisateur est un administrateur.

#### Paramètres :
- **Query Parameters (facultatif) :**
    - `page` (Integer) : Numéro de la page.
    - `limit` (Integer) : Nombre de lieux par page.
    - `userId` (Long) : Identifiant de l'utilisateur effectuant la requête.

#### Réponses :
- **200 OK** : Liste des lieux correspondants.
- **400 Bad Request** : Si l'utilisateur spécifié n'existe pas.
- **403 Forbidden** : Si l'utilisateur n'a pas les permissions nécessaires.

---

## **GET** `/api/locations/{id}`
### Récupérer un lieu par son ID

Renvoie les détails d'un lieu.

#### Paramètres :
- **Path Parameters :**
    - `id` (Long) : Identifiant du lieu.

#### Réponses :
- **200 OK** : Détails du lieu.
- **404 Not Found** : Si le lieu n'existe pas.

---

## **POST** `/api/locations/{locationId}/rate`
### Noter un lieu

Ajoute ou met à jour une note pour un lieu.

#### Paramètres :
- **Path Parameters :**
    - `locationId` (Long) : Identifiant du lieu à noter.
- **Body :**
    - JSON représentant la note contenant :
        - `userId` (Long) : Identifiant de l'utilisateur donnant la note.
        - `ratingValue` (Double) : Valeur de la note (1 à 5).

#### Réponses :
- **200 OK** : Détails de la note ajoutée ou mise à jour.
- **404 Not Found** : Si le lieu ou l'utilisateur n'existe pas.
- **400 Bad Request** : Si les données sont invalides.

---

## **GET** `/api/locations/{locationId}/ratings`
### Récupérer les notes d'un lieu

Renvoie la liste des notes associées à un lieu.

#### Paramètres :
- **Path Parameters :**
    - `locationId` (Long) : Identifiant du lieu.

#### Réponses :
- **200 OK** : Liste des notes.
- **404 Not Found** : Si le lieu n'existe pas.

---

## **PUT** `/api/locations/{locationId}/validate`
### Valider un lieu

Permet à un administrateur de valider un lieu.

#### Paramètres :
- **Path Parameters :**
    - `locationId` (Long) : Identifiant du lieu à valider.
- **Query Parameters :**
    - `adminId` (Long) : Identifiant de l'administrateur.

#### Réponses :
- **200 OK** : Si le lieu a été validé.
- **403 Forbidden** : Si l'utilisateur n'est pas un administrateur.
- **404 Not Found** : Si le lieu ou l'administrateur n'existe pas.

---

## **DELETE** `/api/locations/{locationId}/reject`
### Rejeter un lieu

Permet à un administrateur de rejeter un lieu.

#### Paramètres :
- **Path Parameters :**
    - `locationId` (Long) : Identifiant du lieu à rejeter.
- **Query Parameters :**
    - `adminId` (Long) : Identifiant de l'administrateur.

#### Réponses :
- **200 OK** : Si le lieu a été rejeté.
- **403 Forbidden** : Si l'utilisateur n'est pas un administrateur.
- **404 Not Found** : Si le lieu ou l'administrateur n'existe pas.

---
