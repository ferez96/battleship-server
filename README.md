# Pascal Descendants 2019 - Round 3

## Battle Ship Judge System

> Note: if you use Windows, use `gradlew.bat` instead of `gradlew`

### Requirement

* Java JDK 8
* gradle 4.9
* MinGW or CygWin

### Prepare a match

Run `./gradlew clear` to clean old `battleField/`, or you can just delete the folder yourself.

Run `./gradlew prepare` to create new `battleField/`. In the generated-folder you will find:
* map.txt: a sample map
* ships.txt: a random 10 battle ships for the play

You can change that files like you want 

### Run a match

Copy your and your opponents source code into `player/AI1` and `player/AI2`.

You must have 3 files:
* SET.CPP
* PLAY.CPP
* prices.txt

Run `./gradlew play` and see the result

Have Fun!
