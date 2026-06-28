# Fire

[The Fire model](http://ccl.northwestern.edu/netlogo/models/Fire) is a common [NetLogo](http://ccl.northwestern.edu) example. It studies the percolation of a fire in a forest depending on the density of the forest. This worklow studies the impact of the density factor for a fixed population size. To do this, it perform a design of experiment where the density factor ranges from 20% to 80% by steps of 10.

Since the Fire model is stochastic, the workflow replicates the execution for each instance of the density factor. Results for each replication are be stored it in a CSV file.
