
library(dplyr)
library(ggplot2)

#setwd(paste0(Sys.getenv('CS_HOME'),'/OpenMole/openmole-market/ants-extended'))
#source(paste0(Sys.getenv('CN_HOME'),'/Models/Utils/R/plots.R'))

res <- as.tbl(read.csv('exploration/2018_03_29_11_58_01_LHS_REPETS_LOCAL.csv'))

for(indic in c("food1","food2","food3")){
  g = ggplot(res,aes_string(x = indic,colour="as.character(id)"))
  g+geom_density(alpha=0.3)+scale_color_discrete(name="id")+stdtheme
  ggsave(file = paste0("results/hists_",indic,".png"),width=15,height=10,units='cm')
}



