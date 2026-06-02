# COMP2511 OOP Assignment 2 

This is a DungeonMania-style dungeon crawler built as a Java backend + browser frontend.

The app contains the Java game engine, server API, entity model, goals, battles, inventory, crafting, and dungeon loading.
The client contains the Phaser browser UI that talks to the Java backend over REST.
README.md only points to the official assignment spec, so the code is the actual implementation.

**How to run it**
Backend
From the workspace root:
cd /home/gabi/comp2511/assignment-ii
./gradlew :app:run

Frontend
In another terminal:
cd /home/gabi/comp2511/assignment-ii/client
npm install
npm start

**How to play**
Basic controls
- Move the player with W, A, S, D
- Click on interactable entities to interact with them
- The UI shows your inventory and what you can build

Every player move triggers a game tick. Enemy movement, potion timers, and other world updates happen on each tick. Using an item or building something also advances the game

**Goals**
The game supports map goals such as:

- exit — reach the exit tile
- treasure — collect enough treasure
- boulders — push boulders onto switches
- compound goals via AND / OR

**More notes/interactions**
Items that can be actively used include bombs, potions and weapons (for battle). Players can also interact with entities such as merecenaries, who can be bribed with treasure or mind controlled with a sceptre. 
Walking into an enemy triggers battle, but potion effects such as invincibility and invisibility change enemy movement and combat. 

