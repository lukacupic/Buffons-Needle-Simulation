# Buffons's Needle Simulation
A simple simulator of the [Buffon's needle problem](https://en.wikipedia.org/wiki/Buffon%27s_needle) for approximating the number π.

This simulator uses a [Monte Carlo method](https://en.wikipedia.org/wiki/Monte_Carlo_method) for approximating the number π.
This is done by generating needles at random positions and with random angles on the canvas, which are then thrown onto the canvas itself.

The simulator counts the total number of needles thrown, and the number of needles which have landed on one of the lines. These two values are then plugged into the formula which provides an approximation of the number π.

![alt Awesome LaTeX equation should be displayed here. Sorry if it isn't](https://latex.codecogs.com/gif.latex?%5Cdpi%7B150%7D%20%5Clarge%20%5Cpi%3D%5Cfrac%7B2l%7D%7Bd%7D*%5Cfrac%7Bn%7D%7Bh%7D)

The formula for approximating the number π. ```l``` represents the length of the needle and ```d``` represents the distance between the lines. Values ```n``` and ```h``` represent the total number of needles thrown and the number of those landed on a line. These four values are then used in each step of the simulator to approximate π.
