# Genetic Algos Visualizations based on R

## Motivation

When using [Genetic Algorithms in OpenMole](https://next.openmole.org/Genetic+Algorithms.html) for calibration or optimization, it is important for the user to understand what happens over time.
Graphing the evolution of evolutionary methods is also necessary for communication. 

Basic OpenMole features include the exportation of the populations during the evolution as CSV files.
The web interface of OpenMOLE also helps you to plot these CSV files easily in the user interface. 

A simple way for an user to create graphs is to use the [RTask](https://next.openmole.org/R.html) which enables the usage of R 
from OpenMOLE. The first execution of such a task is slow, because OpenMOLE has to download data from the web and build a local container 
with the necessary programs. But later executions reuse the same container and are quicker. 

Producing graphs automatically after exploration and simulation experiments is part of the methodological recommandations for reproducible research.


## Optimization Problem

To illustrate visualization, we proposed in the [example_of_optimization.oms](./example_of_optimization.oms) a simple function 
to optimize, and a simple and quick optimization. 
This file is imported in other example workflows. 
So any modification you would produce in this file would impact all the other examples.

## Plot Last Iteration 

In the 
 


