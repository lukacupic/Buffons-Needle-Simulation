# Buffons-Needle-Simulation
A simple simulator of the [Buffon's needle problem](https://en.wikipedia.org/wiki/Buffon%27s_needle).

The simulator generates a needle by randomly obtaining a position on the canvas and the angle of the needle. Using a Monte Carlo method, 
the simulator then throws a large number of needles onto the canvas. With every new thrown needle, the simulator takes the number of 
needles which landed on a line and the number of those which didn't, and plugs them into the formula which then provides an approximation of the 
number π.
