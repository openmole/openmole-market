
# fixed parameters
width = 252
height = 252
neighborhood = list(c(-1, 0), c(1, 0), c(0, -1), c(0, 1))

fire <- function(seed, forestDensity, treeResistance){
  set.seed(seed)
  trees = matrix(data = ifelse(runif(n=width*height)<forestDensity,1,0),nrow = height)
  fires = matrix(data=rep(0,width*height),nrow=height)
  fires[sample.int(height,size=1),sample.int(width,size=1)]=1
  
  initialTrees = sum(trees)
  burntTrees = 0
  
  while(sum(fires)>0){
    nextfires = matrix(data=rep(0,width*height),nrow=height)
    for(x in 2:(width-1)){
      for(y in 2:(height-1)){
       if(fires[y,x]==1){
         trees[y,x]=0
         burntTrees = burntTrees + 1
         for(neigh in neighborhood){
           if((trees[y+neigh[2],x+neigh[1]]==1)&&runif(1)>treeResistance){
             nextfires[y+neigh[2],x+neigh[1]] = 1
           }
         }
       } 
      }
    }
    fires = nextfires
  }
  return(list(burnt = burntTrees/initialTrees, binaryBurnt = ifelse(burntTrees/initialTrees>0.5,1,0)))
}


