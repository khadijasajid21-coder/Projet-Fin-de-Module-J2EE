1.	Introduction

Ce document présente l'architecture et la conception technique de la plateforme de gestion de cantine scolaire. Le système a pour objectif de digitaliser l'ensemble du processus de restauration scolaire : consultation des menus, précommande des repas par les étudiants depuis une application mobile Android, et pilotage opérationnel (validation, préparation, service) par les agents et administrateurs de la cantine depuis une interface web.
La plateforme se compose de deux applications clientes s'appuyant sur un backend unique : une application web développée avec Spring Boot et Thymeleaf pour les agents de cantine et les administrateurs, et une API REST sécurisée par JWT destinée à être consommée par une application Android pour les étudiants.
1.1	Objectifs du projetD

●	Permettre aux étudiants de consulter les menus du jour et à venir, et de passer des précommandes depuis leur téléphone.
●	Offrir aux agents de cantine une vue en temps réel des commandes et un suivi de leur statut de préparation.
●	Fournir aux administrateurs des outils complets de gestion des menus, des plats, des utilisateurs et des statistiques.
●	Garantir la sécurité des accès via une authentification par rôle (Administrateur, Agent, Étudiant).
●	Proposer une architecture propre, documentée et maintenable (MVC + couche Service, API REST documentée).
1.2	Acteurs du système
<img width="398" height="114" alt="image" src="https://github.com/user-attachments/assets/86c708c2-e01b-40c9-a5e8-2bee97710c41" />

2.	Architecture générale

L'application suit une architecture en couches classique (MVC + couche Service) commune aux deux points d'entrée : les contrôleurs Web (Thymeleaf, session HTTP classique) pour les agents/administrateurs, et les contrôleurs REST (JSON, authentification JWT stateless) pour l'application Android. Les deux façades s'appuient sur la même couche Service et les mêmes Repositories Spring Data JPA, garantissant une logique métier unique et cohérente.

<img width="261" height="325" alt="image" src="https://github.com/user-attachments/assets/a86de1e4-1fbf-453c-b613-c16a597557f7" />

2.1	Stack technique
<img width="394" height="207" alt="image" src="https://github.com/user-attachments/assets/88717b63-af93-4b55-ab86-d8722a19861e" />

2.2 Organisation des packages

●	entity / entity.enums — Entités JPA et énumérations métier
●	repository — Interfaces Spring Data JPA avec requêtes JPQL personnalisées
●	dto — Objets de transfert utilisés par les formulaires Web et l'API REST
●	service / service.impl — Interfaces et implémentations de la logique métier
●	security — Configuration Spring Security, filtre JWT, UserDetailsService
●	config — Configuration Swagger, initialisation des rôles
●	controller — Contrôleurs Web Thymeleaf (MVC)
●	rest — Contrôleurs REST consommés par l'application Android
●	exception — Gestion centralisée des erreurs de l'API

3.	Diagramme de cas d'utilisation

Le diagramme ci-dessous synthétise les fonctionnalités offertes par le système à chacun des trois acteurs. L'administrateur hérite de l'ensemble des cas d'utilisation de l'agent de cantine, auxquels s'ajoutent la gestion des menus, des plats, des utilisateurs et l'accès aux statistiques.

<img width="292" height="370" alt="image" src="https://github.com/user-attachments/assets/9190136d-eb6a-4be2-a9f1-7c6915325b81" />

<img width="266" height="381" alt="image" src="https://github.com/user-attachments/assets/2a4ea9e5-b47a-4d45-a49d-bb554f527bab" />

4.	Diagramme de classes

Le modèle de données s'articule autour de sept entités JPA principales. Un Utilisateur possède un Role (ADMIN, AGENT ou ETUDIANT) ; lorsque ce rôle est ETUDIANT, l'utilisateur est complété par une fiche Etudiant (matricule, filière, niveau). Un étudiant passe des Commandes, chacune composée de plusieurs LigneCommande référençant un Plat à un prix figé au moment de la commande. Les Menus regroupent des Plats via une relation many-to-many (table de jointure menu_plat).


<img width="179" height="332" alt="image" src="https://github.com/user-attachments/assets/7783a2e2-636c-45ae-a261-2cfedaa8ff22" />

<img width="289" height="341" alt="image" src="https://github.com/user-attachments/assets/4f464040-4d28-44a5-916b-66689bd5f3cb" />

5.	 Sécurité
La sécurité est assurée par Spring Security, configurée avec deux chaînes de filtres distinctes afin de séparer clairement l'authentification Web (session) de l'authentification API (JWT stateless).
5.1	Chaîne Web (/dashboard, /menus, /plats, /commandes, /users)

●	Authentification par formulaire (formLogin) sur /login, avec session HTTP classique.
●	Mots de passe hachés avec BCryptPasswordEncoder.
●	Accès protégé par rôle : /users/** réservé à ROLE_ADMIN ; /menus/**, /plats/**, /commandes/** ouverts à ROLE_ADMIN et ROLE_AGENT.

5.2	Chaîne API (/api/**)

●	Sans état (stateless) : chaque requête doit porter un jeton JWT dans l'en-tête Authorization: Bearer <token>.
●	Le jeton est délivré par POST /api/auth/login et signé en HS256 avec une clé secrète configurée dans application.properties.
●	Un filtre dédié (JwtAuthFilter) valide le jeton et positionne l'utilisateur authentifié dans le contexte de sécurité.
●	/api/menus/** est accessible aux rôles ETUDIANT, AGENT et ADMIN ; /api/commandes/** est réservé au rôle ETUDIANT.

5.3	Rôles applicatifs

<img width="264" height="93" alt="image" src="https://github.com/user-attachments/assets/07491ec1-e5ba-4dbd-8e11-4b92c49171dc" />


<img width="261" height="379" alt="image" src="https://github.com/user-attachments/assets/c1ff7b30-4e67-45a7-b3a4-839bb5696a15" />

<img width="262" height="359" alt="image" src="https://github.com/user-attachments/assets/a5a4e41a-8f1e-488c-a20d-d17453c85e27" />

<img width="262" height="387" alt="image" src="https://github.com/user-attachments/assets/a5f626ad-880c-41bc-b4fd-6e5487029d20" />

9.	Implémentation et interface

9.1 Interfaces web
L’interface web est destinée aux administrateurs et agents de cantine. Elle permet la gestion des menus, des utilisateurs et des commandes.

<img width="751" height="563" alt="image" src="https://github.com/user-attachments/assets/6409c92e-822b-421c-a7d2-9fdd7cdd1e7d" />
Figure 1 : Interface d’authentification

	            Apres de se connecter, un tableau de bord est affiché il représente des statistiques calculées côté serveur (top plats, chiffre d'affaires, commandes par jour/mois) et rendues :
              <img width="840" height="441" alt="image" src="https://github.com/user-attachments/assets/d918dabe-8b2f-49cf-824b-346170299333" />
Figure 2 : Tableau de bord administrateur

<img width="1050" height="549" alt="image" src="https://github.com/user-attachments/assets/f7857339-dd34-43bb-8535-70fe0f5a1e87" />
Figure 3 : Gestion des menus

<img width="1040" height="471" alt="image" src="https://github.com/user-attachments/assets/3be92f86-184d-4eec-a957-69d1ae80537b" />

Figure 4 : Gestion des plats

<img width="994" height="457" alt="image" src="https://github.com/user-attachments/assets/c7c381bd-e4f4-4a64-9b7a-e97a76489ee5" />
Figure 5 : Gestion des commandes
<img width="507" height="247" alt="image" src="https://github.com/user-attachments/assets/7ec86994-8ad6-4949-b144-df329d1cde2f" />

Conclusion

La plateforme développée répond aux besoins fonctionnels identifiés pour la gestion d'une cantine scolaire : un parcours de précommande fluide pour les étudiants, un suivi opérationnel en temps réel pour les agents, et des outils de pilotage complets pour les administrateurs. L'architecture retenue — séparation nette entre contrôleurs Web et contrôleurs REST, couche Service unique, sécurité différenciée par type de client — garantit à la fois la cohérence métier et l'évolutivité du système.












[rapport-cantine-scolaire.pdf](https://github.com/user-attachments/files/29629687/rapport-cantine-scolaire.pdf)
Pesentation sur le projet:
[Uploading Presentation.pptx…]()

