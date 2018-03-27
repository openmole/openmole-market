# Calibration of a NetLogo model

This market entry calibrates the [Ants](http://ccl.northwestern.edu/netlogo/models/Ants) NetLogo model with an Evolutionary/Genetic Algorithm (EA/GA) in OpenMOLE. It proposes 2 approaches, the first on uses a generational NSGA2 which is very classical. The second one is adapted to distributed computing and uses an [island model](http://www.gustafsonresearch.com/thesis_html/node105.html) distribution strategy.


## Calibration objective

To illustrate simply the principle of calibration, we have a one-dimensional calibration objective which cumulates the errors on time needed to exhaust each food reserve.
