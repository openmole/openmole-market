"""
This script calculates the value of the two dimensionnal Ackley function in a point
This script can be used with the optimization algorithm NSGA2.
NSGA2 will look for the set of coordinates (x,y) with the lowest cost.
The global minima is in (0,0)
"""

#CARE command :
#care -o Ackley_function.tgz.bin python Ackley_function.py 0 0

import sys
import numpy as np

#Returns the value of the Ackley function in a point (x,y)
#Please refer to http://benchmarkfcns.xyz/benchmarkfcns/ackleyfcn.html for more informations
def Ackley(x,y):
    return -a*np.exp(-b*np.sqrt((x**2+y**2)/2)) - np.exp((np.cos(c*x) + np.cos(c*y))/2) + a + np.exp(1)

#Ackley function parameters
a = 40          #in [0, 40]   : depth of local minimas. The smaller the deeper.      
b = 2           #in [0, 2]    : width of the central vortex. The smaller, the wider.
c = 0.1*np.pi   #in [0, 2*pi] : number of local minimas

#Catch the input of the run command
#Casting to float is mandatory
x = float(sys.argv[1])
y = float(sys.argv[2])

cost = Ackley(x,y)

#Writing in the output of the process
#Casting to str is mandatory
sys.stdout.write(str(cost))
