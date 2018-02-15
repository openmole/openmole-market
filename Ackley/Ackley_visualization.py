#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Nov 28 13:09:06 2017

@author: leonard
"""

#This file can be used to tune the Ackley function
#Import this file on your system and run it

import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

#Ackley parameters
a = 40          #in [0, 40]   : depth of local minimas. The smaller the deeper.      
b = 2           #in [0, 2]    : width of the central vortex. The smaller, the wider.
c = 0.1*np.pi   #in [0, 2*pi] : number of local minimas

#Calculate the function on a 2D surface defined by x*x
def ackleySurface(x):
    #first term
    t1 = (x*x)
    mt1 = t1[:, np.newaxis] + t1
    #second terme
    t2 = np.cos(c*x)
    mt2 = t2[:, np.newaxis] + t2
    return -a*np.exp(-b*np.sqrt(mt1 /2)) - np.exp(mt2/2) + a + np.exp(1)



x = np.arange(-30, 30, 0.1)
M = ackleySurface(x)

#Projection grid for plot
X, Y = np.meshgrid(x, x) 

#Plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.plot_surface(X, Y, M, cmap = 'coolwarm', linewidth=0, antialiased = True)
plt.show(block = False)
