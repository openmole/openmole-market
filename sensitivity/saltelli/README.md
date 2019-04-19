# Global Sensitivity Analysis.

as exposed in Saltelli et al. 2010 Variance based sensitivity analysis of model output. Design and estimator for the total sensitivity index.

This market entry shows how to use thi SensitivitySaltelli method to compute the first order and total order sentivity indices for a model with multiple outputs.

The model considered is a simple model with first and second order effects and two inputs x1 and x2 and two output values y1 and y2 defined as:

    val y1 = x1 + 0.5 * x2
    val y2 = x1 + 0.5 * x2 + x1 * x2
  
The expected values of first order (SI) and total order (STI) sensitivity indices, given for each pair of input and output (i.e. how much the considered output is sensitive to the considered input) are:

 - y1, x1: SI1 = 4/5, STI1 = 4/5
 - y1, x2: SI2 = 1/5, STI2 = 1/5
 - y2, x1: SI1 = (9 / 4) * (12 / 42) ~= 0.643, 
           STI1 = (7.0 / 36.0) / (40.0 / 144.0) = 0.7
 - y2, x2: SI2 = 12 / 42 ~= 0.286, 
           STI2 = (13.0 / 144.0) / (40.0 / 144.0) ~= 0.325

The openmole script runs the computation of the indices 100 times and reports the average value for each in the console. The values obtained should be close to the previous theoretical expected values given.
