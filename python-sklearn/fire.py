import numpy

# seed = 42
numpy.random.seed(seed)

width, height = 252, 252

#forest_density = 0.65
#tree_resistance = 0.1

burnt_trees = 0

# setup
neighborhood = ((-1, 0), (1, 0), (0, -1), (0, 1))

trees = numpy.zeros((width, height))
trees[1:width-1, 1:height-1] = numpy.random.random(size=(width-2, height-2)) < forest_density

initial_trees = sum(sum(trees))

fires = numpy.zeros((width, height))
fires[numpy.random.randint(1, width), numpy.random.randint(1, height)] = 1

# run

while sum(sum(fires)) > 0 :
    nextfires = numpy.zeros((width, height))
    for x in range(1, width - 1):
        for y in range(1, height - 1):
            if fires[x, y]==1 :
                trees[x, y]=0
                burnt_trees=burnt_trees+1
                for dx, dy in neighborhood :
                    if trees[x+dx, y+dy]==1 and numpy.random.random() > tree_resistance :
                        nextfires[x+dx, y+dy]=1
    #print(sum(sum(trees)))
    fires = nextfires

burnt = burnt_trees / initial_trees
binaryburnt = 0.0
if burnt > 0.5:
    binaryburnt = 1.0

print("burnt = "+str(burnt))
print("binary_burnt = "+str(binaryburnt))
