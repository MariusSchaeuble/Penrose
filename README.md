# Penrose
Create a Penrose tiling using decomposition from any start formation.

The main method defines a start formation, in the given case the ace. 

n determines the steps of decomposition, n=0 means original formation. 

Some methods (For example shown in Wikipedia) use the half dart inflation: Assume, that a tile of generation n+1 can never be in more than one tile of gen. n.
That only works for the start formations sun and star, and is hence a strong heuristic.

My function newGeneration works for any valid start formation, like single tiles or the ace. The general case must take into account, that new tiles can stretch over several old tiles. To avoid 
conflicts betwen tiles or inaccuracy due to multiple defined objects, I used java pointers to compare existing objects, and not its values/points.

