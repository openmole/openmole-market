# Screening with the Morris method

This market entry shows how to use the [Morris sensitivity analysis method](https://en.wikipedia.org/wiki/Morris_method) for the [screening](https://en.wikipedia.org/wiki/Sensitivity_analysis#Screening) of a model in OpenMOLE.

Several OpenMOLE workflows are proposed here:
* "MorrisVerify.oms" is provided so anyone can ensure the method works as expected (see below for details)

## Verify 

"MorrisVerify.oms" is provided so anyone can ensure the implementation of the method works as expected. 
It also stands as a good starting point to understand the intuition of the method. 

In this simple workflow, there are 5 float inputs `a`,`b`,`c`,`d` and `e`. 

Three functions are evaluated: 
```
x = 2b + 3c - d^2 + e^3
y = 10a + 2bc + 3bcd
z = a
```

At execution time, results are stored into a subdirectory named "results_verify"


function x is designed such as variables should no lead to indirect effect, so impact of sigma(anything,x)=0 (except for powers)

A sensitivity analysis should detect as a result: 
* a has no direct impact on x (mu=0) and no indirect effect through another variable (sigma=0)
* b has a direct impact on x (mu=2b) and no indirect effect (sigma=0)
* c has a direct impact on x (mu=3c) and no indirect effect (sigma=0)
* d has a strong impact on x (mu=d^2) and an indirect effect due to the power (sigma>0)
* e has a strong impact on x and an indirect effect due to the power (sigma>0)

function y should lead to strong interactions between some variables:
* a has a direct impact (mu=a) and no indirect effect (sigma 0)
* b has direct impact and an indirect effect (sigma >> 0)
* c has a direct impact which also depends on another variable (sigma >> 0)
* d has a direct impact and also depends on another variable (sigma >> 0)

A good way to understand the role of levels and repetitions count is to reproduce this experiment with different parameters. 

## Traffic Basic 

The "MorrisTraffic.oms" workflow applies the Morris method to the [Traffic Basic NetLogo model](http://ccl.northwestern.edu/netlogo/models/TrafficBasic)

However, as this model is stochastic, the results might be kind of unstable. 

Note the impact of changing the parameters for levels and repetitions. Also notice how, when the parameter `verbose = true` is used, messages in the simulation results explain how the computation of the indicators is made. 

