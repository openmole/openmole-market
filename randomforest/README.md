# Random Forest image classifier #

This workflow explores the parameters of a random forest image classifier written in *Python* using *scikit-learn*.

The classifier is trained against the leaves dataset publicly available from here: [http://www.vision.caltech.edu/Image_Datasets/leaves/](http://www.vision.caltech.edu/Image_Datasets/leaves/)
The idea is to distinguish the leaves of 3 different species.

The Python script receives 3 input parameters:
  - the location of the dataset
  - the number of trees in a forest
  - the depth of each tree
  
The OpenMOLE workflow explores those 3 parameters as follows:
  - the input dataset is shuffled so that it's picked up in a different order by the Python script
  - the number of trees ranges from 5 to 25 by steps of 5
  - the tree depth ranges from 3 to 18 by steps of 3
  
The output of the Python script is a double precision floating point number representing the precision accuracy.

All the computed accuracies are stored in a CSV file using a hook for later post-processing.

Two environmments are defined (Slurm and Condor) to delegate the execution of the Python script.

Finally, the Python task was packaged using CARE with the resulting archive being available along this workflow. It should 
run seamlessly on any Linux platform.  
The original Python script is also available for records purpose.
