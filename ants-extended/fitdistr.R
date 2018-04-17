
library(fitdistrplus)


getDistribType <- function(x,distrnames = c("norm", "lnorm", "exp", "gamma", "unif","logis"),criteria="ks"){
  # other potential distribs (need additional parameters depending on the estimation method)
  #,"weibull")
  fits = list()
  for(distrname in distrnames){
    fits[[distrname]] = fitdistrplus::fitdist(x,distr = distrname,method = "mme")
  }
  
  # available goodness of fit statistics
  #gofstat(fits)
  #gofstat(fits)$kstest
  #gofstat(fits)$cvmtest
  #gofstat(fits)$adtest
  
  gof = gofstat(fits)[[criteria]]
  return(distrnames[gof==min(gof)])
  
}


# tests
# x = rnorm(1000,mean= 10,sd = 1)
# getDistribType(x)
# 
# xln = exp(x)
# getDistribType(xln)
# 
# xexp = rexp(1000)
# getDistribType(xexp) # note : an exp is a particular case of gamma with shape = 1
# xgamma = rgamma(1000,shape=10)
# getDistribType(xgamma)


