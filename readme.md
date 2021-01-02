## Fluffy bird

Game inspired by "Flappy bird" game

<img src="./readme_images/gameplay.png?raw=true" alt="Alt text" style="zoom:50%;" />

#### Rules

There are 3 birds on the screen:
- Orange - player's bird
- Green - weak bot bird
- Blue - decent bot bird

Birds are constantly flying forward with a speed that increases as they get more and more points

Birds can also change their vertical velocity to fly upwards or downwards because they get points for dodging the pipes that appear in front of them and there always is a small gap between top and bottom pipe.

Goal can be to either beat the bots or get as high score as one can get

#### Controls

- W - fly upwards
- S - fly downwards
- D - double the horizontal velocity while the key is held
- R - reset
- Esc - pause

#### Technicals

- Bots are implemented using fuzzy logic with help of jFuzzyLogic library
- Game is written in Java and UI is made with Swing
- Under directory 'runnable' there is a .jar file launching the game that you can run with a command:
```
java -jar FluffyBird.jar
```
