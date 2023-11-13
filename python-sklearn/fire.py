import numpy

width, height = 252, 252

#density = 0.65#forest_density
#resistance = 0.1#tree_resistance

burnt_trees = 0

# setup
neighborhood = ((-1, 0), (1, 0), (0, -1), (0, 1))

trees = numpy.zeros((width, height))
trees[1:width-1, 1:height-1] = numpy.random.random(size=(width-2, height-2)) < tree_density

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
binary_burnt = burnt > 0.5

print("burnt = "+str(burnt))
print("binary_burnt = "+str(binary_burnt))
