
import sys
import numpy
from numpy import *
from array import *
import csv

input = open(sys.argv[1],'r')
n = float(sys.argv[2])

print("reading the matrix")
data = csv.reader(input)

array = numpy.array(list(data)).astype(float)

print(array)
print(n)
mult = array * n

print("saving the matrix")
numpy.savetxt(sys.argv[3], mult, fmt='%g')


