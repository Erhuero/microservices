# RealmServices - Architecture Microservices

## Description du Projet

RealmServices est une plateforme de microservices développée en Java avec Spring Boot et Spring Cloud. Le projet implémente une architecture distribuée pour la gestion de cartes à jouer, incluant des services de vérification de fraude et de notification.

## Architecture Globale

Le projet suit une architecture microservices avec les composants suivants :

- **Eureka Server** : Service de découverte et d'enregistrement des microservices
- **API Gateway** : Point d'entrée unique pour toutes les requêtes
- **Service Cartes** : Gestion des decks et des cartes
- **Service Fraud** : Vérification anti-fraude des joueurs
- **Service Notification** : Envoi de notifications
- **Service AMQP** : Gestion des messages RabbitMQ
- **Service Clients** : Clients Feign pour la communication inter-services

## Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Docker et Docker Compose
- PostgreSQL
- RabbitMQ

## Installation et Configuration

### 1. Cloner le Repository

```bash
git clone <url-du-repository>
cd realmservices
```

### 2. Configuration de l'Environnement

Créer un fichier `.env` à la racine du projet :

```bash
PGADMIN_DEFAULT_EMAIL=admin@domain.com
PGADMIN_DEFAULT_PASSWORD=admin
```

### 3. Vérification des Prérequis

```bash
# Vérifier Java
java -version  # Doit être Java 17+

# Vérifier Maven
mvn -version   # Doit être Maven 3.6+

# Vérifier Docker
docker --version
docker-compose --version

# Vérifier les ports disponibles
netstat -an | grep -E "(5432|5672|8080|8081|8082|8083|8761|15672|9411|5050)"
```

### 4. Démarrage des Services d'Infrastructure

Lancer les services de base avec Docker Compose :

```bash
# Démarrer l'infrastructure
docker-compose up -d postgres pgadmin rabbitmq zipkin

# Vérifier que les services sont démarrés
docker-compose ps

# Vérifier les logs
docker-compose logs postgres
docker-compose logs rabbitmq
```

### 5. Construction des Microservices

Compiler tous les services :

```bash
# Compilation complète
mvn clean install -DskipTests

# Ou compilation par module
mvn clean install -pl eureka-server -am
mvn clean install -pl apigw -am
mvn clean install -pl cartes -am
mvn clean install -pl fraud -am
mvn clean install -pl notification -am
```

### 6. Démarrage des Microservices

#### Option 1 : Démarrage Local

```bash
# Démarrer Eureka Server
cd eureka-server
mvn spring-boot:run

# Démarrer API Gateway
cd ../apigw
mvn spring-boot:run

# Démarrer Service Cartes
cd ../cartes
mvn spring-boot:run

# Démarrer Service Fraud
cd ../fraud
mvn spring-boot:run

# Démarrer Service Notification
cd ../notification
mvn spring-boot:run
```

#### Option 2 : Démarrage avec Docker

```bash
# Construire les images Docker
mvn clean package -DskipTests

# Démarrer tous les services
docker-compose up -d
```

## Ports des Services et Communication

### Ports Utilisés

| Service | Port | Description | Accès Externe |
|---------|------|-------------|---------------|
| Eureka Server | 8761 | Service de découverte | http://localhost:8761 |
| API Gateway | 8083 | Point d'entrée API | http://localhost:8083 |
| Service Cartes | 8080 | Gestion des cartes | http://localhost:8080 |
| Service Fraud | 8081 | Vérification fraude | http://localhost:8081 |
| Service Notification | 8082 | Notifications | http://localhost:8082 |
| PostgreSQL | 5432 | Base de données | localhost:5432 |
| PgAdmin | 5050 | Interface admin PostgreSQL | http://localhost:5050 |
| RabbitMQ | 5672 | Message broker | localhost:5672 |
| RabbitMQ Management | 15672 | Interface admin RabbitMQ | http://localhost:15672 |
| Zipkin | 9411 | Traçage des requêtes | http://localhost:9411 |

### Communication Inter-Services

**Flux de Requêtes** :
1. **Client** → **API Gateway (8083)** → **Service Spécifique**
2. **Service Cartes** → **Service Fraud** (via Feign Client)
3. **Service Cartes** → **Service Notification** (via RabbitMQ)
4. **Eureka Server** : Registre central de tous les services

**Réseaux Docker** :
- `spring` : Communication entre microservices
- `postgres` : Accès aux bases de données

## Structure du Projet

```
realmservices/
├── eureka-server/          # Service de découverte
├── apigw/                  # API Gateway
├── cartes/                 # Service de gestion des cartes
├── fraud/                  # Service anti-fraude
├── notification/           # Service de notifications
├── amqp/                   # Configuration RabbitMQ
├── clients/                # Clients Feign
├── k8s/                    # Configurations Kubernetes
└── docker-compose.yml      # Orchestration Docker
```

## API Endpoints et Interactions

### Service Cartes (Port 8080)

**Endpoints** :
- `POST /api/v1/deck` - Enregistrer un nouveau deck

**Interactions** :
1. **Réception** : Requête d'enregistrement de deck
2. **Validation** : Vérification des données du deck
3. **Persistance** : Sauvegarde en base PostgreSQL
4. **Notification** : Envoi de message RabbitMQ au service Notification
5. **Réponse** : Confirmation de l'enregistrement

**Schéma de Données** :
```json
{
  "deckName": "string",
  "cards": ["string"],
  "playerId": "UUID"
}
```

### Service Fraud (Port 8081)

**Endpoints** :
- `GET /api/v1/fraud-check/{playerId}` - Vérifier si un joueur est frauduleux

**Interactions** :
1. **Réception** : Requête de vérification anti-fraude
2. **Vérification** : Analyse du comportement du joueur
3. **Historique** : Enregistrement de la vérification en base
4. **Réponse** : Statut frauduleux ou non

**Schéma de Réponse** :
```json
{
  "isFraudster": "boolean",
  "timestamp": "datetime",
  "playerId": "UUID"
}
```

### Service Notification (Port 8082)

**Endpoints** :
- `POST /api/v1/notification` - Envoyer une notification

**Interactions** :
1. **Réception** : Message RabbitMQ du service Cartes
2. **Traitement** : Préparation de la notification
3. **Envoi** : Transmission via le canal approprié
4. **Persistance** : Sauvegarde de la notification en base

**Schéma de Données** :
```json
{
  "toPlayerId": "UUID",
  "message": "string",
  "type": "string"
}
```

### Flux de Communication

```
[Client] → [API Gateway:8083] → [Service Cartes:8080]
                                    ↓
                              [Service Fraud:8081] (via Feign)
                                    ↓
                              [Service Notification:8082] (via RabbitMQ)
```

**Détails des Interactions** :
- **Feign Client** : Communication synchrone entre services
- **RabbitMQ** : Communication asynchrone pour les notifications
- **Eureka** : Découverte automatique des services
- **Zipkin** : Traçage de toutes les interactions

## Configuration des Profils

Le projet supporte plusieurs profils Spring Boot :

- **default** : Configuration locale
- **docker** : Configuration pour conteneurs Docker
- **kube** : Configuration pour Kubernetes

## Déploiement Kubernetes

### Architecture Kubernetes

Le projet inclut des configurations Kubernetes complètes dans le dossier `k8s/` organisées en deux parties :

#### 1. Infrastructure de Base (`k8s/minikube/bootstrap/`)

**PostgreSQL** :
- `configmap.yml` : Configuration de la base de données
- `service.yml` : Service ClusterIP pour PostgreSQL
- `statefulset.yml` : Déploiement persistant de PostgreSQL
- `volume.yml` : Stockage persistant pour les données

**RabbitMQ** :
- `configmap.yml` : Configuration du message broker
- `rbac.yml` : Rôles et permissions Kubernetes
- `services.yml` : Services pour RabbitMQ (AMQP + Management)
- `statefulset.yml` : Déploiement de RabbitMQ

**Zipkin** :
- `service.yml` : Service pour le traçage distribué
- `statefulset.yml` : Déploiement de Zipkin

#### 2. Services Métier (`k8s/minikube/services/`)

**Services Applicatifs** :
- `cartes/` : Déploiement du service de gestion des cartes
- `fraud/` : Déploiement du service anti-fraude
- `notification/` : Déploiement du service de notifications

### Déploiement

```bash
# 1. Démarrer Minikube
minikube start --driver=docker

# 2. Activer les addons nécessaires
minikube addons enable ingress
minikube addons enable metrics-server

# 3. Déployer l'infrastructure
kubectl apply -f k8s/minikube/bootstrap/

# 4. Attendre que l'infrastructure soit prête
kubectl wait --for=condition=ready pod -l app=postgres --timeout=300s
kubectl wait --for=condition=ready pod -l app=rabbitmq --timeout=300s

# 5. Déployer les services métier
kubectl apply -f k8s/minikube/services/

# 6. Vérifier le déploiement
kubectl get pods
kubectl get services
```

### Accès aux Services

```bash
# Accéder au dashboard Kubernetes
minikube dashboard

# Accéder aux services via port-forward
kubectl port-forward service/cartes-service 8080:8080
kubectl port-forward service/fraud-service 8081:8081
kubectl port-forward service/notification-service 8082:8082

# Accéder à PostgreSQL
kubectl port-forward service/postgres-service 5432:5432

# Accéder à RabbitMQ Management
kubectl port-forward service/rabbitmq-management 15672:15672
```

## Bases de Données et Stockage

### PostgreSQL

**Configuration** :
- **Utilisateur** : `lysero`
- **Mot de passe** : `password`
- **Base de données** : Créée automatiquement
- **Port** : 5432
- **Volume persistant** : `/data/postgres`

**Tables par Service** :
- **Service Cartes** : `deck`, `carte`
- **Service Fraud** : `fraud_check_history`
- **Service Notification** : `notification`

**Accès via PgAdmin** :
- **URL** : http://localhost:5050
- **Email** : `pgadmin4@domain.com`
- **Mot de passe** : `admin`

### RabbitMQ

**Configuration** :
- **Port AMQP** : 5672
- **Port Management** : 15672
- **Utilisateur par défaut** : `guest`
- **Mot de passe par défaut** : `guest`

**Queues Utilisées** :
- **Service Cartes** → **Service Notification** : Queue de notifications
- **Service Fraud** : Queue de vérifications anti-fraude

**Accès Management** :
- **URL** : http://localhost:15672
- **Utilisateur** : `guest`
- **Mot de passe** : `guest`

## Monitoring et Observabilité

### Zipkin - Traçage Distribué

**Fonctionnalités** :
- Traçage des requêtes entre microservices
- Visualisation des chaînes d'appels
- Analyse des performances et latences
- Détection des goulots d'étranglement

**Accès** : http://localhost:9411

### Eureka - Service Discovery

**Fonctionnalités** :
- Registre central de tous les microservices
- Découverte automatique des services
- Monitoring de la santé des services
- Load balancing automatique

**Accès** : http://localhost:8761

### Logs et Monitoring

**Logging** :
- **Framework** : SLF4J + Lombok
- **Format** : Logs structurés JSON
- **Niveaux** : INFO, WARN, ERROR, DEBUG

**Métriques** :
- **Actuator** : Endpoints de santé et métriques
- **Prometheus** : Métriques applicatives
- **Grafana** : Tableaux de bord (optionnel)

## Développement

### Ajout d'un Nouveau Service

1. Créer un nouveau module Maven
2. Ajouter la dépendance au POM parent
3. Configurer le profil Spring Boot
4. Ajouter la configuration Docker et Kubernetes

### Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter les tests d'un module spécifique
cd <module>
mvn test
```

## Dépannage et Maintenance

### Problèmes Courants

#### 1. Ports Déjà Utilisés

```bash
# Identifier les processus utilisant les ports
lsof -i :5432  # PostgreSQL
lsof -i :5672  # RabbitMQ
lsof -i :8080  # Service Cartes
lsof -i :8081  # Service Fraud
lsof -i :8082  # Service Notification
lsof -i :8083  # API Gateway
lsof -i :8761  # Eureka Server

# Arrêter un processus spécifique
kill -9 <PID>
```

#### 2. Problèmes de Base de Données

```bash
# Vérifier la connectivité PostgreSQL
docker exec -it postgres-realmservices psql -U lysero -d postgres

# Vérifier les logs PostgreSQL
docker logs postgres-realmservices

# Redémarrer PostgreSQL
docker-compose restart postgres
```

#### 3. Problèmes RabbitMQ

```bash
# Vérifier la connectivité RabbitMQ
docker exec -it rabbitmq rabbitmqctl status

# Vérifier les queues
docker exec -it rabbitmq rabbitmqctl list_queues

# Redémarrer RabbitMQ
docker-compose restart rabbitmq
```

#### 4. Problèmes de Services

```bash
# Vérifier la santé d'un service
curl http://localhost:8080/actuator/health  # Service Cartes
curl http://localhost:8081/actuator/health  # Service Fraud
curl http://localhost:8082/actuator/health  # Service Notification

# Vérifier l'enregistrement Eureka
curl http://localhost:8761/eureka/apps
```

### Logs et Monitoring

#### Logs Docker

```bash
# Voir les logs d'un service Docker
docker logs <nom-du-conteneur>

# Suivre les logs en temps réel
docker logs -f <nom-du-conteneur>

# Logs de tous les services
docker-compose logs

# Logs d'un service spécifique
docker-compose logs cartes
docker-compose logs fraud
docker-compose logs notification
```

#### Logs Kubernetes

```bash
# Logs d'un pod spécifique
kubectl logs <nom-du-pod>

# Logs en temps réel
kubectl logs -f <nom-du-pod>

# Logs de tous les pods d'un service
kubectl logs -l app=cartes
```

### Maintenance

#### Sauvegarde Base de Données

```bash
# Sauvegarde PostgreSQL
docker exec postgres-realmservices pg_dump -U lysero postgres > backup_$(date +%Y%m%d_%H%M%S).sql

# Restauration
docker exec -i postgres-realmservices psql -U lysero postgres < backup_file.sql
```

#### Nettoyage Docker

```bash
# Nettoyer les conteneurs arrêtés
docker container prune

# Nettoyer les images non utilisées
docker image prune

# Nettoyer les volumes non utilisés
docker volume prune

# Nettoyage complet
docker system prune -a
```

## Contribution

1. Fork le projet
2. Créer une branche feature
3. Commiter les changements
4. Pousser vers la branche
5. Créer une Pull Request

## Licence

Ce projet est sous licence propriétaire de Lysero.

## Support

Pour toute question ou problème, veuillez contacter l'équipe de développement Lysero.
