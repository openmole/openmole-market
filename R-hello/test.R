args<-commandArgs(trailingOnly = TRUE)
x<-as.numeric(args[1])
print(x)
data<-read.csv("data.csv",header=T,sep=",")
write.csv(data*x,"results.csv")

