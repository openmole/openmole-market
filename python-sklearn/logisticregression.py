from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
import pandas
import numpy

X=pandas.DataFrame({'density':forest_density_array, 'resistance':tree_resistance_array})
Y=pandas.DataFrame({'binaryburnt':binaryburnt_array})

X_train, X_test, Y_train, Y_test=train_test_split(X,Y)

clf = LogisticRegression(random_state=0, solver='lbfgs').fit(X_train, Y_train)

pred = clf.predict(X_test)

# define outputs - must be "standard types", not objects (basic types and multidimensional lists)
# here: values of parameters for which prediction fails
errdensity = X_test['density'][abs(pred - Y_test['binaryburnt'])==1].values.flatten().tolist()
errresistance = X_test['resistance'][abs(pred - Y_test['binaryburnt'])==1].values.flatten().tolist()

# performance of the surrogate
score = clf.score(X_test,Y_test)
