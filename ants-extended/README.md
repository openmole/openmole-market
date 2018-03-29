# Extended exploration of ants

This market entry extends the [Ants](http://ccl.northwestern.edu/netlogo/models/Ants) NetLogo model (see the corresponding market entry) and illustrates some questions can be asked to a simple model, using simple sampling methods but with heterogenous tasks. In particular, we show the coupling of the exploration task with an RTask.

## Extended model

The model ants is extended, in particular on the following points :
  - supplementary synthetic setups were added : the chooser `setup-type` allows to choose between `fixed` the original setup, `kernel-mixture` a random spatial mixture of exponentials for food density which is then thresholded
  - hidden parameters were put as explicit parameters : `wiggle-angle` is angle smoothing the random walk of ants
  - to study stationary states, an option `infinite-food?` was added, which toggle results in fixed quantities of food regardless of what the ant collect
  - additional indicators were added : for example `carrying-efficiency` evaluates the efficiency of food collection (distance walked to collect food / total distance walked)
  - corrected implementation choices prone to bugs (for example using display to store state of agents), refactorization in "classes" *NetLogo models do not necessary have to be quick and dirty and the langage can be used safely for large models* **TODO : still huge implementation bias in scent procedure : examples of artifacts in path heatmap // others hidden parameters**

## Distribution of indicators

![histograms food1](results/hists_food1.png)
