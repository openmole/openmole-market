# Python fire model
Keywords: python, DirectSampling, logistic regression
## Basic description of the model
A model of a forest fire that can start at a given location and propagate throughout the forest according to basic rules. We are interested in studying the dynamics that lead to a majority of the forest being burnt. The environment of the forest is represented as a rectangle grid in which the cells that can contain or not a tree, the density of the forest is given by the parameter *density*. 

#### Dynamics: 
A fire will start at a random location. At each step, the fire will propagate to the neighborhing cells with a probability linked to the resistance of the trees. The resistance of the trees is given by the parameter *resistance*.
If a fire is present on the cell, the tree is burnt and dies. 
The proportion of the forest that is burnt is computed at each step as well as the binary variable that stipulates whether the majority has burnt or not.
 
#### Input of the simulation: 
- *density*
- *resistance*
- *height*
- *width*


#### Output: 
- proportion of burnt trees: *burnt*
- majority of forest burnt: *binaryburnt* 

## Exploration scenarios: DirectSampling
### Objective of the exploration
Suppose the forest model has been validated and effectively represents the kind of forest fires we are interested in studying. 
We would now like to understand what different fire regimes can be envisaged. The first question that can therefore be raised is:
- *Q1: Given different parameter sets, how far will the fire propagate at the end of the simulation?*

To answer this question, we would like to explore the different fire scenarios that can be considered with the model. By changing the value of the input parameters, we will observe a variety of fire outcomes. As a first, intuitive step, we would therefore like to carry out a **DirectSampling**: sampling in the input space to observe the general behavior of the forest system through the value of *binaryburnt*.

### Step 1: Embedding the python model


In order to explore the ABM model with OpenMole, we need to integrate or embed it in the OM environment. This requires 3 substeps: 
- (1) defining the model variables in the OM environment
- (2) loading the model script in OM
- (3) mapping the OM variables to the python variables. 

For this step to be carried out, we need to make sure our python model is headless: can be manipulated from the outside. In other words, we want the global variables to be left undefined in the python script. For this tutorial, the fire model is provided in *fire.py*. 

The embedding is done with a PythonTask:
```scala=1
val ptask=PythonTask()set()
```



```scala=1
//(1) Define the OM variables with the type
val forest_density = Val[Double]
val tree_resistance = Val[Double]
val replication = Val[Int]

val burnt = Val[Double]
val binaryburnt = Val[Double]

//Embed the model with a python task
val simmodel = PythonTask(// (2) This is where the script is given and the libraries that are needed for the model to run are specified.
    workDirectory / "fire.py",libraries=Seq("numpy")
  ) set (//(3) This is where the OM variables are linked to the python ones and the specific output that we want to study are specified.
    (inputs,outputs) += (forest_density,tree_resistance,replication),
    inputs += forest_density mapped "forest_density",
    inputs += tree_resistance mapped "tree_resistance",
    inputs += replication mapped "seed",
    outputs += burnt mapped "burnt",
    outputs += binaryburnt mapped "binaryburnt"
  )

```
### Step 2: Preparing the exploration task:

Once the python model is embedded in OM, it can be explored with the DirectSampling method. Each method will require different specifications. In this case, we need to tell OM:
- the model that should be evaluated, here this is simmodel
- how to sample in the input space: what range for each variable parameter, the step used to explore the space.
- finally, because the model is stochastic, how many times we want the same parameter set to be run (how many replications).  

```scala=1
val sampler = DirectSampling(
    evaluation = simmodel,
    sampling =
      ((forest_density in (0.1 to 0.9 by 0.2)) x
       (tree_resistance in (0.1 to 0.9 by 0.2)) x
       (replication in (UniformDistribution[Int](max = 100000) take 5)))
  )

```
Once run, the results of the DirectSampling can be directly observed using the **hook** methods. However, in this example, we would like to analyse more closely how the output varies and how the outcome is related to the different parameters. For this, we build another python script with the objective of analysing the data generated from the simulations. 

## Analysing the DirectSampling results with Python
### Objective of the analysis
Once we get the results of the sampling, we want to study the nature of the relationship between *density*, *resistance* and the outcome *binaryburnt*. In particular, we would like to validate the hypothesis that the relation is logistic. Furthermore, we want to identify the transition regions: if there exists critical values of *density* and *resistance* for which the fire regime changes. The question raised here is: 
- *Q2: What is the nature of the relationship between the propagation of the fire and the density or resistance of the forest? Can it be described by a logistic function?*
- *Q3: Are there any identifiable transitions from a non burnt state to a burnt state, and can they be described with the parameters?*


**Idea**: We want to test the performance of a logistic regression of the output *binaryburnt* on the predictors *density* and *resistance*. The model we are testing is given by : p(Y)= $\alpha$density + $\beta$replication. We also want to know where the transition is by identifying for what values of the parameters the model is not able to correctly predict the outcome. To do this analysis we need to build a python model that retrieves the data from the DirectSampling task, analyses it and yields an output that can be visualised in OM. 

### Step 1: From OM output to Python input
The data analysed with the python script is the output of the DirectSampling. We therefore need to give python the data in a readable format. The **hook** method cannot be used here because it is called at the end of a workflow, and not between tasks. Various options can work: create a csv file from the DirectSampling output or read the output as array in the python script. 

```python-repl=
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
import pandas
import numpy

X=pandas.DataFrame({'density':forest_density_array, 'resistance':tree_resistance_array})
Y=pandas.DataFrame({'binaryburnt':binaryburnt_array})
```
Where *forest_density_array*,*tree_resistance_array* and *binaryburnt_array* will be mapped to the output variables of the DirectSampling. The script is provided in this tutorial, see *logisticregression.py*. 

 
### Step 2: Embedding the python script
We now need to embed this analysis script in the same way as the fire model script. The variables considered here are arrays because the DirectSampling yields output for every set of parameters.
```scala=1
// Python task to embed the machine learning analysis used to evaluate the behavior of the computationnal model
//  (surrogate logistic regression model)
val sklearnclassifier = PythonTask(
    workDirectory / "logisticregression.py",
    libraries = Seq("pandas","numpy","scikit-learn")
) set (
    inputs += forest_density.array mapped "forest_density_array",
    inputs += tree_resistance.array mapped "tree_resistance_array",
    inputs += binaryburnt.array mapped "binaryburnt_array",
    outputs += errdensity mapped "errdensity",
    outputs += errresistance mapped "errresistance",
    outputs += score mapped "score"
) hook (workDirectory / "classiferrors.csv")


```
We use the same synthax of PythonTask() followed by set(). However this corresponds to the end of the workflow, so we want to retrieve the results to be able to visualise them in a file or in OM. We use the **hook** method for this. This will store the results (errdensity, errresistance and score) in a csv file called *classiferrors.csv*. 


### Step 3: Building and running the workflow 
A workflow in OM is determined by the order in which the different tasks should be carried out. A workflow is therefore given be the tasks and the specific transition type between each one. In this case, we run the *sampler* task and then analyse it with the *logistic regression* of the Python task. The workflow can be described by:

```scala=1
// Workflow: exploration to sklearn analysis transition
//(">-" aggregates the sampling as arrays for the sklearnclassifier task, where inputs arrays are retrieved using the ".array" function of prototypes)
sampler >- sklearnclassifier

```

![Workfow visualisation](https://github.com/openmole/openmole-market/blob/dev/python-sklearn/workflow.png)


## Summary

In this tutorial we learnt:
- How to embed a model in OM with the python task,
- How to create an exploration task to run simulations of the model. We used the DirectSampling method.
- How to link the exploration task to an Python analysis of results, again embedding a logistic regression analysis in python using the PythonTask. 

