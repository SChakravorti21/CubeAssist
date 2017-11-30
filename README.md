# CubeAssist
Android App that allows users to solve a Rubik's Cube by providing a solution after choosing from 3 input types

## Motivation
My main motivation for creating this app was to learn about Android app development.
Since I had already created a console Java program that outputted a solution to a cube given a text scramble,
I figured I could learn a thing or two about Android by converting the console version.

_Secondary motivation_: I noticed that the app store is quite lacking in apps that ***teach*** users how to solve
the cube. While it watching YouTube videos to learn is fine, an interactive way of learning the process is
even better (in my opinion).

## How it Works
There is a Navigation Bar to help get around the app, whose layout is arranged through fragment transactions
and/or creating Activity intents. The solution to the cube is animated in 2D graphics with an "unfolded"
cube using custom views.

Notes: 

____The Camera Mode will not run until the user manually grants it permissions in settings.____

____Cube may not appear consistently on different screen sizes yet____ (I am working on standardizing the views).

### Input Modes
1) **Text input/Random Scramble Generation**
- Allows the user to enter a ___custom text scramble___ adhering to WCA standards
- Allows the user to generate a ___random scramble___ and follow along
- ___TAPPING ON THE CUBE___ plays and pauses the solution animation
- ___Swiping from right to left on the cube___ moves back one step
- ___Swiping from left to right on the cube___ moves forward one step
- The view for this mode is the same as the solution view that all other
fragments redirect to
- Allows user to ___reset___ the solution process, as well as ___skip___ through
phases of the process
2) **Manual Color Input**
- Provides user instructions as to hold the cube while inputting colors
- User can easily ___move between sides___ of the cube
- User can ___rotate___ sides without having to re-input all colors
- ___Simple palette___ is available to the user to pick colors to input
- ___Simple error checking___: the app will check Edge-Orientation and Corner-Orientation
to ensure that an inputted cube is valid
3) **Camera Mode**
- ___Instructions___ for using this mode can be hidden and unhidden from the top of the view
- User is shown where to align the cube using a ___yellow grid___ on a SurfaceView
- ___Side selection drop-up menu____ allows user to select which side they are taking an image of
- Pressing the ___solve___ button redirects to Manual Input mode for user to double-check
that all colors are consistent

## Media
#### Video 1
(Shows the usage of the camera mode to generate a solution for a real life cube! Skip through
the parts of me double-checking and adjusting certain entries if you wish.)

[![Video 1](https://img.youtube.com/vi/3-0eWPKy4wQ/0.jpg)](https://www.youtube.com/watch?v=3-0eWPKy4wQ)

#### Video 2
(Shows random scramble generation, solution speed adjustment, and skipping around phases.)

[![Video 2](https://img.youtube.com/vi/uzidIbOFh0o/0.jpg)](https://www.youtube.com/watch?v=uzidIbOFh0o)

#### Video 3
(Shows how a custom text scramble can be inputted.)

[![Video 3](https://img.youtube.com/vi/ezP5nDz701Y/0.jpg)](https://www.youtube.com/watch?v=ezP5nDz701Y)

#### Video 4
(Shows a regular solution at a moderately fast speed.)

[![Video 4](https://img.youtube.com/vi/xr5y_r-MfeE/0.jpg)](https://www.youtube.com/watch?v=xr5y_r-MfeE)
