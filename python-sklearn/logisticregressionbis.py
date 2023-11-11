from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split 
import pandas
import numpy

#d = pandas.read_csv('data/training.csv')
#dp = pandas.read_csv('data/validation.csv')

#X = d[['density','resistance']]
#y = d['binaryburnt']
#print(X)
X=pandas.DataFrame({'density':density_array, 'resistance':resistance_array})
Y=pandas.DataFrame({'binaryburnt':binaryburnt_array})

X_train, X_test, Y_train, Y_test=train_test_split(X,y)

clf = LogisticRegression(random_state=0, solver='lbfgs').fit(X_train, Y_train)

pred = clf.predict(X_test)

#inds = (numpy.where(abs(pred - yp)==1))[0].tolist()
#print(inds)

prederror = dp.loc[abs(pred - Y_test)==1]

print(prederror['density'])
print(prederror['resistance'])

# define outputs - must be "standard types", not objects (basic types and multidimensional lists)
errdensity = list(prederror['density'])
errresistance = list(prederror['resistance'])

score = clf.score(X_test,Y_test)
print(score)
