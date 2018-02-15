This tutorial contains three scripts dedicated to the use of python CARETask in openmole.
We propose here to run some optimization scripts on the Ackley function
Please refer to http://benchmarkfcns.xyz/benchmarkfcns/ackleyfcn.html for more details on this function.

You will find in this folder

-"Ackley_visualization.py" : this script produces a plot of the 2D Ackley function. It allows you to 
  tune this function and shape it as you wish.

-Ackley_function is usefull for testing the compilation with CARE and the basics of NSGAII. 
	-"Ackley_function.py" returns the value of the Ackley function for a position in a 2D space.
	  The CARE command is in the script 
	-"Ackley_function.tgz.bin" is the result of the CARE command
	-"Model.oms" allows you to search for minimas of the Ackley function with the NSGAII algorithm.

-Ackley_SA is a more complicated demonstration of how Open Mole can be used to complete difficult tasks.
	-"Ackley_SA.py" is an implementation of the Simulated Annealing algorithm. The algorithm searchs
	  for a minima of the Ackley function but it depends on a few hyperparameters. We want to find
	  the best set of these hyperparameters in order to find the global minima of the Ackley function
	  as fast as possible and with a high probability.
	  The CARE command is in the script 
	-"Ackley_SA.tgz.bin" is the result of the CARE command
	-"Model_SA.oms" allows you to search for good combinations of hyperparameters of the SA algorithm.

-Ackley_SA_niche takes it further. We will do the same task as with the Ackley_SA example but we want to run
	this optimization for several parametrizations of the Ackley function. The Ackley function, which is
	the problem we want to solve, is parametrized by three numbers, (a, b, c), and we now want to know 
	if the configuration of the SA algorithm depends of the problem we are interested in.
	- "Ackley_SA_niche.py" is the same implementation as in "Ackley_SA.py" but now the parameters of the
	   Ackley function can be given as inputs
	   The CARE command is in the script 
	-"Ackley_SA_niche.tgz.bin" is the result of the CARE command
	-"Model_SA_niche_1.oms" allows you to search for good combinations of hyperparameters of the SA algorithm
	  given a discrete set of parameters (a,b,c).
	-"Model_SA_niche_2.oms" allows you to search for good combinations of hyperparameters of the SA algorithm
	  in a continuous space for the set of parameters (a,b,c).
