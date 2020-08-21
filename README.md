# openmole-market

The OpenMOLE marketplace contains complete examples of real world workflows.

They are maint to be concise with the main focus put on the workflow script.
Still they should be easily reusable and any material required by the workflow (scripts, binaries, input data, ...) should be provided to make sure a **Minimum Working Example** is available.

## How to contribute ##

  - Fork this repository
  - Create a new folder for your workflow example
  - Add all the required dependencies and a subset of the input data
  - Edit the top-level README.md to add your new entry
  - Create a Pull Request for us to review your submission

All the accepted submissions will be part of the market place and will be compiled against the last version of OpenMOLE. The available entries are also available from the OpenMOLE website: [http://next.openmole.org/Documentation_Market%20Place_All.html](http://next.openmole.org/Documentation_Market%20Place_All.html)

Note: if you are asked for a password for "gitlab.openmole.org" when pushing your changes into your fork of the repository, you might simply add the `--no-verify` flag to your git push.

## Available workflows ##

  - [Advanced methods](https://github.com/guillaumecherel/TutorialEAForModelling): advanced methods for calibrating, validating and analyzing complex systems models.
  - [Ants model](ants): a NetLogo model calibrated using the Evolutionary/Genetic Algorithms.
  - [Approximate Bayesian Computation](abc): an example of how to use perform approximate bayesian computation in OpenMOLE.
  - [Extended ants model](ants-extended): using a RTask to do statistics on the extended NetLogo ant model.
  - [Fire simulation](fire): a fire simulation model in NetLogo with a design of experiments studying its density factor
  - [FSL segmentation](fsl-fast): brain segmentation using [FSL](https://fsl.fmrib.ox.ac.uk/fsl/fslwiki)
  - [Java hello world](java-hello): an example of how to embed Java code in OpenMOLE
  - [OpenMOLE plugin](hello-plugin): two workflows using two different OpenMOLE plugins
  - [Pi Monte Carlo approximation](pi): a workflow using a ScalaTask to approximate the value of pi. The Design of Experiments changes the seed of the random number generator.
  - [Python sklearn](python-sklearn): python task using the machine learning library sklearn.
  - [R hello world](R-hello): an example of how to embed R code in OpenMole.  This workflow executes an R program with 100 different inputs, makes a computation, and saves to a file.
  - [Random Forest classifier](randomforest): This workflow explores the parameters of a random forest image classifier written in Python using scikit-learn.
  - [SimPopLocal model](simpoplocal): a geographical model calibrated using genetic algorithms.
  - [Sensitivity-Screening analysis](sensitivity/morris): a method to quickly analyze which inputs are influential on large spaces of parameters.
  - [Global Sensitivity Analysis](sensitivity/saltelli): a variance based sensitivity analysis of model output.
  - [Test Functions for NSGA2](nsga2-test-functions): reference functions to double check the correctedness of the NSGA2 algorithm, and also view examples of usage of NSGA2
  
