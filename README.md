# Buffons's Needle Simulator
A simple simulator of the [Buffon's needle problem](https://en.wikipedia.org/wiki/Buffon%27s_needle) for approximating the number π.

The simulator uses a [Monte Carlo method](https://en.wikipedia.org/wiki/Monte_Carlo_method) for approximating π.
This is done by generating needles at random positions and with random angles, which are then thrown onto the canvas.

![alt The canvas](http://mathworld.wolfram.com/images/eps-gif/BuffonNeedle_700.gif)

In each step, the simulator counts the total number of needles thrown, and the number of needles which have landed on one of the lines. These two values are then plugged into the formula which provides an approximation of π.

![alt Awesome LaTeX equation should be displayed here. Sorry if it isn't!](https://latex.codecogs.com/gif.latex?%5Cdpi%7B150%7D%20%5Clarge%20%5Cpi%3D%5Cfrac%7B2l%7D%7Bd%7D*%5Cfrac%7Bn%7D%7Bh%7D)

The above formula is used for approximating π. ```l``` represents the length of the needle and ```d``` represents the distance between the lines. Values ```n``` and ```h``` represent the total number of needles thrown and the number of those landed on a line. These four values are then used in each step of the simulator to approximate π.
