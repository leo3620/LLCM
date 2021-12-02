Projet réalisé par Léo LECLERC et Clement MOISAN:

Ce projet a pour objectif l'implémentation d'une interface du Predictive Risk Model Algorithm dont vous pourrez trouver la source ici :
https://www1.health.gov.au/internet/main/publishing.nsf/Content/predictive-risk-model-algorithm


* Pour lancer le projet il faut d'abord compiler le projet backend via Maven en utilisant la commande :

A la racine du projet:

  mvn clean package

 On peut ensuite lancer le back via la création d'une image docker:

  docker build -t llcm .
  docker run -p 8080:8080 llcm


* Pour le front-end:

Toujours a la racine du projet :
  docker build -t llcm-front LLCM-front
  docker run -d -p 80:80 llcm-front

* Vous pouvez retrouver le projet à l'adresse http://localhost:80

N'oubliez pas de supprimer les processus docker à la fin via la commande habituelle docker ps puis docker stop



