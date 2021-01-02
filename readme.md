## Fluffy bird

Game inspired by "Flappy bird" game

![Alt text](./readme_images/gameplay.png?raw=true)

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

- Bots are implemented using fuzzy logic. Bots' source codes can be found under resources/fcl
- Game is written in Java and UI is made using Swing
- Under directory 'runnable' there is a .jar file that you can run with command
```
java -jar FluffyBird.jar
```
