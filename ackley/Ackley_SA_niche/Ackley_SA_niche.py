"""
This script is an implementation of the Simulated Annealing algorithm (SA) on the Ackley function
The goal here is to use the NichedNSGA2 algorithm to look for the best
hyperparameters of the SA algorithm for any parametrization of the Ackley function
NichedNSGA2 will look for the set of hyperparameters (T0, cexp, pas, niter)
such that (cost, niter) are minimum in the space of the parameters (a, b, c)
"""

# command : 
# python Ackley_SA_niche.py 0 0 0 0 1 1 1 42

import sys
import numpy as np

#Set the random seed for numpy
np.random.seed(int(sys.argv[8]))

#Returns the value of the Ackley function in a point (x,y)
#Please refer to http://benchmarkfcns.xyz/benchmarkfcns/ackleyfcn.html for more informations
def Ackley(x,y):
    return -a*np.exp(-b*np.sqrt((x**2+y**2)/2)) - np.exp((np.cos(c*x) + np.cos(c*y))/2) + a + np.exp(1)

#The parameters of the Ackley function
a = float(sys.argv[5])          #in [0, 40]   : depth of local minimas. The smaller the deeper. 
b = float(sys.argv[6])          #in [0, 2]    : width of the central vortex. The smaller, the wider.
c = float(sys.argv[7])*np.pi    #in [0, 2*pi] : number of local minimas

#The hyperparameters we intend to tune
#All casts are mandatory
T0 = float(sys.argv[1])
cexp = float(sys.argv[2])
pas = float(sys.argv[3])
niter = int(float(sys.argv[4]))

##### Initialization of the SA algorithm
# "width" define the area in which the initial position of the algorithm is randomly sampled
width = 30
# Random choice of the initial position centered on 0
x, y = (np.random.rand()-0.5)*width, (np.random.rand()-0.5)*width
# Initialize the value of the Ackley function
cost = Ackley(x, y)
# Initialize the temperature
T = T0

##### Simulated Annealing Algorithm
for i in range(niter):
    #Choose randomly a move, dx, dy in [-pas, pas]
    dx, dy = pas * 2*(np.random.rand() - 0.5), pas * 2*(np.random.rand() - 0.5)
    #Calculate new value of the Ackley function
    newCost = Ackley(x+dx, y+dy)
    #Move if new value is better than old
    if newCost < cost:
        x += dx
        y += dy
        cost = newCost
    #If not
    else:
        #Draw a random number
        r = np.random.rand()
        #Accept the move if this random number is smaller than the Boltzman cost of the move
        #Here we choose to check if the temperature is not too low to avoid 0 division
        if T > 10e-300 and np.exp((cost-newCost)/T) > r:
            x += dx
            y += dy
            cost = newCost
    #Update temperature
    T = T0*np.exp((cexp-1)*i)

#Write result in the output of the process
#You only get the last result accepted here, not the best found in all the algorithm
sys.stdout.write(str(cost))
