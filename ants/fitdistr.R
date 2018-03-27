
library(fitdistrplus)

distrnames = c("norm", "lnorm", "exp", "gamma", "unif","logis")#,"weibull")

fits = list()
for(distrname in distrnames){
  fits[[distrname]] = fitdistrplus::fitdist(ATV,distr = distrname,method = "mme")
}

names(gofstat(fits))
gofstat(fits)$kstest
gofstat(fits)$cvmtest
gofstat(fits)$adtest
