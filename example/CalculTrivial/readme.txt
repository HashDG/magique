Ce package a pour objectif d'illustrer l'utilisation
de m�thodes synchrones et asynchrones avec Magique

Le probl�me est trivial :
on a deux agents nomm�s f et g qui renvoient un entier
au bout d'un certain temps. Le superviseur doit r�cup�rer
les deux entiers renvoy�s et en faire la somme .

En synchrone ce temps mis pour ce calcul est la somme du temps
de f + le temps de g tandis qu'en asynchone ce temps est
le max(f,g).

Cette application a �t� r�gl�e avec des temps de 10s pour f 
et 15s pour g.
