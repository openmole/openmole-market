
# Parallel estimation of Pi 

This workflow exposes how to compute an estimation of Pi usign a [Monte-Carlo estimation of Pi](https://en.wikipedia.org/wiki/Monte_Carlo_method){:target="_blank"}. The computation task is a Scala task using a self contained code. The workflow computes parallely 100 independant executions of this task using randomly generated seeds for the random number generator. It produces 100 independant realisation of the Monte-Caro estimation. Then the workflow gathers them in a vector and computes an average on the values of the vector. Finally the result is displayed on the standard output.

