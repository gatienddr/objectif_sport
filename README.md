# Projet_ObjectifsSportifs

## Gatien DIDRY - Groupe IOT 

### Edito  
	Bienvenue sur mon projet Android effectué dans le cadre de ma Licence Professionnelle.
	
	Le but de ce projet était de créer une application de suivi d'entraînement pour des sports mesurable en distance (type course à pied, vélo etc...) et/ ou mesurable en temps (type yoga).  
	L'utilisateur peut entrer les sports qu'il pratique, ses objectifs et ses entraînements.
	Il peut aussi mesurer en direct son entraînement grâce à une fonctionnalité qui calcule la distance (grâce au GPS) qu'il parcourt et son temps.
	Puis il peut afficher son trajet sur une carte.
	
	Le projet était hebergé sur un autre GIT (interne à l'IUT), il est normal qu'il n'y ait pas plus de commit.
	
### Version en production : https://play.google.com/store/apps/details?id=projet.iutlp.projet_objectifssportifs

### Me contacter : gassddr@gmail.com


## Fonctionnalité et outils mis en place : 

### 1er commit d'initialisation : 04/11/20 

### Base de données SQLite  : 08/11/20  
        Mise en place de la base de donnée SQLite avec premières méthode principales(d'ajout, de suppression etc)...
        J'ai choisi de commencer par la base de données pour avoir des un squelette solide sur lequel je peux travailler.


### Ecran principale : 22/11/20
        Mise en place de l'ecran principale avec plusieurs recyclerView (pour les objectifs, les sports et les entraînements) et une barre de navigation qui selon le click affiche un recycler view et masque les autres. Ca fonctionne de manière semblable à une single page application même si je ne pense pas que ce ne sera pas le cas ensuite.
        J'ai renommé les "activités" du sujet en "entrainements" pour ne pas qu'il y ait confusion entre les activity Java et les activités sportives.

### Fenetre de choix : 22/11/20 (branch develop/menuAjout)
        Mise en place d'une fenetre de type Dialog proposant à l'utilisateur d'ajouter un entrainement/objectif/demarrer une activité etc...


### Ajout d'un sport : 28/11/20 (branch develop/ajoutSport)

        Mise en place du menu d'ajout d'un sport. Pour la liste des sport, je n'ai pas trouvé d'API rassemblant beaucoup de sport pour être utilisé, j'ai donc mis un grand nombre de sport dans un tableau dans une classe du package "utilitaire". Ca me permet d'etre bien plus exaustif.

### Ajout d'un objectif : 15/01/21 (branch develop/ajoutObjectifs)
        Mise en place de la fonctionnalité d'ajout d'objectifs. J'ai fait cela dans une activité dédié car il y a beaucoup d'item graphique à mettre en place. 


### Ajout d'un entrainements : 19/01/21 (develop/ajoutActivitees)
		Mise en place de la fonctionnalité d'ajout d'entrainements. J'ai fait cela dans une activité dédié car il y a beaucoup d'item graphique à mettre en place. L'activité est très ressemblant à l'ajout d'objectif.

### Modification de l'affichage de la liste des entrainements : 19/01/21 (develop/modifAffichageListeEntrainements)
		Modification de la vue des entrainements et objectif pour adapter selon si le sport est mesurablr en distance/temps
		
### Modification de la classe objectif, ajout des pourcentages : 20/01/21 (branch develop/pourcentageObjectif)
		Modification de la classe objectif, ajout de 2 attributs décrivant la mesure de l'accomplissement d'un objectif.

### Modification affichage objectif pour que l'utilisateur puisse visualiser sa progression : 25/01/2021  (branch develop/pourcentageObjectif)
		Modification de l'affichage des objectifs pour que l'utilisateur puisse visualiser sa progression selon l'objectif
		
### Mise en place de l'entrainement chronometré : 03/02/2021 (branch feature/entrainementGPS)
		Mise en place de l'activité d'entrainement en direct. La distance est calculé grâce au GPS, le temps grâce au temps du système. Toutes ces données sont affiché à l'utilisateur.
		Un thread parallèle graphique a du etre mis en place pour simuler un chronometre visuel pour l'utilisateur. A la fin de son entrainement, l'athlète peut enregistrer son travail.
		
### Ajout de style personnalisé : remplacement du floating action button en text view personnalisé : 03/02/2021 (branch majStylePerso)
		Mise en place de style dans le fichier style.xml pour centraliser le code.
		
### Suppression d'un objectif : 05/02/2021 (branch develop/suppObj)
	Suppression d'un objectif possible lorsque l'on clique longuement dessus
	
### Modification d'un objectif : 06/02/2021(branch develop/modifObj)
	Modification d'un objectif possible lorsque l'on clique longuement dessus

### Suppression d'un entrainement : 06/02/2021(branch develop/suppEntrainement)
	Suppression d'un entrainement en mettant a jour les pourcentages des objectifs
	
### Modification d'un entrainement : 07/02/2021(branch develop/ModifEntrainement)
	Modification d'un entrainement en mettant a jour les pourcentages des objectifs

### Suppression d'un sport : 08/02/2021 (branch develop/suppSport)
	Suppression d'un sport en mettant a jour les nettoyant aussi les objectifs et les entrainements

### Modification d'un sport : 08/02/2021 (branch develop/modifSport)
	Modification d'un sport en mettant a jour tous les recyclerView
	
### Mise en place des tests Expressos :10/02/2021 (branch develop/newTests)
	Mise en place des test expresso : ATTENTION ! Pour que 100% des tests expresso passent, il faut déjà lancer l'app, y ajouter un sport, 
	y ajouter un entrainement et y ajouter un objectif. Autoriser le GPS pour l'application aussi.
	Je n'ai pas réussi à le faire directement dans le @before des tests (ça crash), il faut donc le faire à la main.
	
### Mise en place de la notification lorsqu'un objectif est atteint : 11/02/2021 (branch develop/notification)
	Lancement d'une notification lorsque l'on accompli un objectif qui fécilite l'utilisateur.
	
### Mise en place de la map lorsque l'utilisateur clique sur un entrainement : 21/02/2021 (branch develop/map)
	Mise en place de la map OSMdroid lorsque l'utilisateur clique sur un entrainement, il peut afficher le parcour qu'il a effectuer.
	On affiche une map vide lorsqu'il n'y a pas de données.
	
	