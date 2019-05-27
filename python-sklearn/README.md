# Python and scikit-learn

This market entry illustrates how to use the scikit-learn package inside a `PythonTask`.

It plugs a modified netlogo fire model which has an additional tree resistance parameter (probability to ignite when it deterministically should in the classical model) with a PythonTask running a basic logistic classifier on model outputs. This way, simulation model behavior is learnt and it can be replaced by the regression model as in this particular case the fit is very good (phase transition in the fire model). We output here parameter values for which prediction failed, for example to understand the phase transition line in the two dimensional parameter space.
