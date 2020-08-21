# Test Functions for NSGA2 

## Why Test? 

Metaheuristics for optimization such as genetic algorithms leave a huge freedom to developers for their implementation.
Genetic Algorithms such as NSGA2 are even less trivial. 
Their implementation in the [mgo](https://github.com/openmole/mgo) used in [OpenMole](https://openmole.org/) was adapted so they are efficient for computation on clusters or grids. 
This leaves a lot of uncertainty to the user on what really happens.
Why should you, as a user, trust this implementation?
The answer is simple: you should not. 
Any implementation in scientific computing should be verified. 


## Test Functions

Many functions named "Test Functions" were proposed over time to test and compare multi-objective optimization algorithmls.
The [wikipedia page](https://en.wikipedia.org/wiki/Test_functions_for_optimization) lists several of them.

A test function is an optimization function which you can propose as the problem to optimize to your implementation.
Functions test:
  - problems with multiple parameters and multiple objectives
  - problems with constraints: a part of the space of parameter leads to computation errors as a result, so the algorithm should achieve to deal with it
  - problems with parts of the space of solution that are tricky to detect, either because they are statistically unlikely to find, or because they are is a part of the space of solutions which is heavily constrained, etc.

Test tunctions enable the testing of several aspects including:
  - coverage of the Pareto front,
  - speed of convergence

Once a test function was explored by an implementation of an algorithm, you should compare the Pareto front obtained withou your implementation with the one expected in literature.


## Test and Learn 

Test functions have another role: they constitute a simple example of optimization problem.
As a user, you can also learn to tune the parameters of the genetic algorithm as check when convergence 
occurs.

## Test Workflows for OpenMole

We provide here a few examples of test functions which you can open with OpenMole. 

For each workflow:
  - Choose a test function.
  - Run the workflow.
  - Open the files with the solutions, check you understand them. 
  - Graph them and compare them with the literature (you have these results in the [wikipedia page](https://en.wikipedia.org/wiki/Test_functions_for_optimization)).


