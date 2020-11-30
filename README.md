# Baby Lizards Problem Genetic Algorithm

A Genetic Algorithm to solve the Baby Lizards Problem, which is a twist on the classic N-Queens problem.

Written using Java and complete with a GUI made with Java Swing that displays found solutions.


## Demo
<img src="https://github.com/briannamcdonald/baby-lizards-problem-GA/blob/main/images/demo.png">


## Problem Description
In the N-Queens problem that is similar to the Baby Lizards Problem, you must place N queens on an NxN chessboard such that no two queens are in the same row, column, or diagonal. 

The Baby Lizards Problem is a twist on this where N lizards must be placed in an NxN grid-like nursery that also contains immovable trees in predefined positions. When two lizards are in the same row, column, or diagonal, the lizards can eat each other, **unless there is a tree between them**. The goal is to place the lizards such that no lizards can be eaten.

The presence of trees changes the solution drastically from the solution of the N-Queens problem, as many of the possible optimizations for the N-Queens problem no longer work in this case. Further description of this problem as well as another way to solve it can be found here: [How to solve the Baby Lizards Problem — a fun twist on the N-Queens problem](https://www.freecodecamp.org/news/how-to-solve-the-baby-lizards-problem-a-fun-variant-on-the-n-queens-problem-a6980f5e72a/).


## Solution Description
See SolutionDescription.pdf.


## To-Do
* Allow certain parameters to be passed in as command line arguments.
* Test more parameter values to improve speed and accuracy when finding solutions for larger board sizes.
* Finish writing unit tests for each component of the GA.
