
from sklearn.linear_model import LogisticRegression
import pandas
import numpy

d = pandas.read_csv('data/training.csv')
dp = pandas.read_csv('data/validation.csv')

X = d[['density','resistance']]
y = d['binaryburnt']
#print(X)

Xp = dp[['density','resistance']]
yp = dp['binaryburnt']

clf = LogisticRegression(random_state=0, solver='lbfgs').fit(X, y)

pred = clf.predict(Xp)

#inds = (numpy.where(abs(pred - yp)==1))[0].tolist()
#print(inds)

prederror = dp.loc[abs(pred - yp)==1]

print(prederror['density'])
print(prederror['resistance'])

# define outputs - must be "standard types", not objects (basic types and multidimensional lists)
errdensity = list(prederror['density'])
errresistance = list(prederror['resistance'])

score = clf.score(Xp,yp)
print(score)



