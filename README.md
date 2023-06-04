# Shinro Solver & Helper

Le Shinro Solver & Helper est une application Java qui permet de résoudre et d'aider à la résolution des grilles de jeu Shinro.

## Installation

1. Importer le projet dans votre IDE java.
2. Importer la librairie choco-solver-4.10.12.jar du dossier lib dans le projet.

## Utilisation

1. Exécuter la méthode main de ShinroSolver.java pour lancer le solveur.
2. Exécuter la méthode main de ShinroHelper.java pour lancer le helper.

Vous pouvez modifier les paramètres de la grille dans le main des classes ShinroSolver.java et ShinroHelper.java. Vous trouverez des exemples de grilles dans le fichier exemple-grille-shinro.txt avec la syntaxe à respecter si vous souhaitez créer vos propres grilles.

Sachez que vous pouvez fournir une grille qui comporte déjà des billes, le solveur ne les prendra pas en compte. Il ne prendra en compte que les contraintes globales de la grille. Le helper lui prendra en compte les billes déjà placées dans la grille. Il vous donnera des indices sur les billes à placer dans la grille ou si une ou des billes sont mal placées.

## Explication des paramètres de grille

La configuration de la grille de jeu est définie à l'aide de trois paramètres : shinroGridState, rowBalls et columnBalls :

`shinroGridState` est un tableau 2D qui représente la grille du jeu Shinro. Chaque élément du tableau correspond à une case de la grille. Les valeurs dans ce tableau peuvent être :

- 0 : une case vide.
- 1 : une case contenant une bille.
- 2 : une flèche pointant vers la droite.
- 3 : une flèche pointant vers la gauche.
- 4 : une flèche pointant vers le haut.
- 5 : une flèche pointant vers le bas.
- 6 : une flèche pointant vers le haut-droite.
- 7 : une flèche pointant vers le haut-gauche.
- 8 : une flèche pointant vers le bas-droite.
- 9 : une flèche pointant vers le bas-gauche.

`rowBalls` et `columnBalls` sont des tableaux qui définissent respectivement le nombre de billes dans chaque ligne et chaque colonne de la grille. `rowBalls` est un tableau qui définit le nombre de billes dans chaque ligne de la grille, de haut en bas. De la même manière, `columnBalls` est un tableau qui définit le nombre de billes dans chaque colonne de la grille, de gauche à droite.

Ces trois tableaux permettent de définir les contraintes globales pour le placement des billes dans la grille.
