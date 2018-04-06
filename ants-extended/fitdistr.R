
library(fitdistrplus)

distrnames = c("norm", "lnorm", "exp", "gamma", "unif","logis")#,"weibull")

fits = list()
for(distrname in distrnames){
  fits[[distrname]] = fitdistrplus::fitdist(groundbeef$serving,distr = distrname,method = "mme")
}

gofstat(fits)
gofstat(fits)$kstest
gofstat(fits)$cvmtest
gofstat(fits)$adtest
