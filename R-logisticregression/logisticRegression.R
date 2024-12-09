
n = length(forest_density_array)
train = sample.int(n,size=0.75*n,replace = F)

d = data.frame(density = forest_density_array[train],tree_resistance_array[train],binaryburnt=binaryburnt_array[train])

logitmodel = glm(formula = binaryburnt ~ density + resistance, data = d, family = binomial(link = 'logit'))

dtest = data.frame(density = forest_density_array[-train],tree_resistance_array[-train],binaryburnt=binaryburnt_array[-train])

prediction = predict.glm(logitmodel,newdata=dtest,se.fit = FALSE)

score = (1 - abs(prediction - dtest$result))/nrow(dtest)
errdensity = dtest$density[abs(prediction - dtest$result)==1]
errresistance = dtest$resistance[abs(prediction - dtest$result)==1]
