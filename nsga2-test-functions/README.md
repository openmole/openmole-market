# Test Functions for NSGA2 

## Why Test? 

Metaheuristics for optimization such as genetic algorithms leave a huge freedom to developers for their implementation.
Genetic Algorithms such as NSGA2 are even less trivial. 
Their implementation in the [mgo](https://github.com/openmole/mgo) used in [OpenMole](https://openmole.org/) was adapted so they are efficient for computation on clusters or grids. 
This leaves a lot of uncertainty to the user on what really happens.
Why should you, as a user, trust this implementation?
The answer is simple: you should not.
Any implementation in scientific computing should be verified to be trusted.


## Test Functions

Many functions named "Test Functions" were proposed over time to test and compare multi-objective optimization algorithms.
The [wikipedia page](https://en.wikipedia.org/wiki/Test_functions_for_optimization) lists several of them.

A test function is an function  you can use as the problem to optimize, for which the expected results are known.
Test functions might test:
  - problems with multiple parameters and multiple objectives
  - problems with constraints: a part of the space of solutions can not be computed, so the algorithm should achieve to deal with it
  - problems with parts of the space of solution that are tricky to detect, either because they are statistically unlikely to find, or because they are in a part of the space of solutions which is heavily constrained, etc.

The results of a test function are usually compared in both terms of:
  - coverage of the Pareto front,
  - speed of convergence


## Test and Learn 

Test functions have another role: they constitute a simple example of optimization problem.
As a user, you can also learn to tune the parameters of the genetic algorithm as check when convergence 
occurs.


## Test Workflows for OpenMole

We provide here a few examples of test functions which you can open with OpenMole. 

  - ConstrEx
  - CP1
  - Schaffer N1
  - Schaffer N2


To run a test 
  - Choose one of the workflows starting with "test function"
  - Run the workflow
  - Update the view on the left using the "refresh" button
  - Download the graph of the last Pareto front, and compare it with the literature (you might use the [wikipedia page](https://en.wikipedia.org/wiki/Test_functions_for_optimization) )

You might then tune the parameters of the optimization algorithm and analyze the results, to understand how to better use the optimization method.

