# Assignment II Part 1: Blog Template

## Task 1) Code Analysis and Refactoring ⚙️

### a) From DRY to Design Patterns (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/1)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

[Answer]
    The move methods are quite large, especially in Mercenary and ZombieToast which also use switch statements which is a common code smell. Moreover there is a lot of shared code in the move methods in both Mercenary and ZombieToast - the case "random" in zombietoast is the same as the case "invisible" for mercenary, and the case "runAway" in zombietoast is the same as the case "invincible" for mercenary. 
    onMovedAway and onOverlap seems to be redunandant in zombietoastspawner since they're just returning.



> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

[Answer]
    Implementing the strategy pattern would improve the quality of the code and avoid repetition. Strategy patterns encourages separation concerns, which is useful in this context since each enemy class can be focused on ochestration of movement rather than catering for each type of scenario that can change movement for an enemy (e.g., if the player is under the influence of a potion, if theres something blocking the movement etc). It also enables open-closed principles, so new methods than can influence movement can be added without changing the existing code in each enemy class. 


> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

[Briefly explain what you did]

    I created a MovementStrategy interface with a move(Game game, Enemy enemy) method. Each movement type (RunAwayMovement, RandomMovement, HostileMovement etc) is its own class implementing this interface. Each enemy then holds a reference to this strategy.
    I created the following movement types:
        - RandomMovement
        - RunAwayMovement
        - HostileMovement
        - AlliedMovement

    I decided that the move method for spiders did not need its own strategy pattern(s) because its movement was fixed and not influenced by the player. Therefore, since only Mercenarie and zombietoasts' movement is influenced by the player, they are the only enemies that require strategy patterns for their movements. 

    I then refactored Mercenary.java and ZombieToast.java to align with my strategy pattern. 

### b) Inheritance Design (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/2)

> i. List one design principle that is violated by collectable objects based on the description above. Briefly justify your answer.

[Answer]
    SRP (single responsibility principle) is violated as instead on focusing on one functionality/responsibility, collectable items like wood and treasure have responsibilities/functionalities for unrelated methods that do things like track durabality and apply buffs. This makes the classes harder to maintain.


> ii. Refactor the inheritance structure of the code, and in the process remove the design principle violation you identified.

[Briefly explain what you did]
    I implemented the refactoring method 'Extract Class' where i made classes Buff.java and Durability.java. Then in InventoryItem.java, the abstract methods for durability and buff become public methods that returned the appropriate results depending if a collectable item has durability or buff-ness or not. If a collectable did have a durability/buff effect, then it was found and returned using the Buff.java and Durability.java classes. Then I could delete instances of these abstract methods from collectables that didnt care about durability and buff effects (i.e., in Wood.java, Key.java, Arrow.java, Bomb.java and Treasure.java). 
    In collectable items that did have durability and buff effects, the durability and buff of that item was set in its constructor where it made new instances of objects of Buff.java and Durability.java. The changes made to these fields were now no longer a concern of these collectables, but rather of their according classes. 

### c) Open-Closed Goals (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/3)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]
    The Open/Closed principle (OCP) states that s/ware entities should be open for extension but closed for modification. The design of Goal.java and GoalFactory.java is not good quality because it does not comply with OCP. This is because if the requirements of a certain goal type changed then you would need to modify the switch statement or if you were to add a new goal type youd have to add a new case to all the switch statements in both files, hence modifying both classes, directly going against OCP. So yes the design should be changed.    


> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]
    The design pattern I chose was a composite pattern because simple goals can be represented as leaves and composites can be the connectors (i.e., AND and OR) since they conjoin goals. This design is also good since each compound goal can contain exactly 2 subgoals so then it ends up looking like a tree. 
    Refactoring just saw me make files for all the types of goals and separating theem into their according files, mainly copying the logic that was already in Goal.java for each type of goal. I changed Goal.java to an interface with the 2 methods achieved and toString which all the goal types implemented. GoalFactory.java then mainly stayed the same - i just changed each case to call their respective file instead of creating a new general goal. 

### d) Open Refactoring (12 marks)

[Merge Request 1](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/4)

[Briefly explain what you did]
    In Entity, the abstract methods onOverlap, onMovedAway and onDestroy, are forced to be implemented in all of its subclasses. Howwever, in classes such as Exit, exits cannot be destroyed so such methods can be redunant in certain cases in some of its subclasses. 

    This is a violation of ISP??? EVEN THO ENTITY ISNT AN INTERFACE???

    To correct this, i made the abstract methods onOverlap, onMovedAway and onDestroy each their own interface. I didnt group them in a single interface (like entityInteractions) because some entities dont use all 3 methods. E.g., boulders can overlap with things but cant be destroyed. Then i went through all of its subclasses and deleted instances of the methods if they weren't being used. I then edited the GameMap and added checks for places where it called the methods where i added small instanceof checks before calling them.


[Merge Request 2](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/5)

[Briefly explain what you did]
    Im Inventory.java there is a violation of SRP because the inventory is concerned with inventory management and the crafting system. The crafting in itself violates OCP to add new recipes and new items to build you have to modify the inventory code. To fix this, I wanted to separate the crafting logic so that it was outside of Inventory.java using an abstract factory design pattern. 

    What i wanted was:
        - inventory concerned with what the player has 
        - recipe checks if the player actually has the stuff to craft, and then initiates the craft (so then removes the materials they use to craft too)
        - actual buildables factories crafts the item

    I made a CraftingFactory interface and then a BowFactory and a ShieldFactory. While doing this I discovered buildables are constructed from EntityFactory which is like a god factory because it seems to construct all possible entities. Whilst this factory in itself is poor design because its doing way too much and makes OCP difficult to maintain, since this MR focuses on buildables/inventory cleanup, I'll just focus on just that for now. 

    I made bow and shield factories instead be responsible for the construction of bows and shields rather than entity factory. Having different factories for all types of buildables helped fulfill OCP because if new buildables were added to the system, you just make a new factory for that buildable.
    Then I made a Receipe.java class so that it had somewhere to call the appropriate factories based on the item the player wanted to craft. it checks if the player has the required items and then removes it from their inventory.

    Then i needed to fix Game.java because getBuildables functionality moved to recipe - same with ResponseBuilder. This meant modifying the getBuildables method in Player to instead pull the data from recipe. This saw me add a method in Recipe called canCraftItems which basically did what was previously implemented but now was in receipe - it returned a lsit of all the items the player could craft based off of their inventory.

    As I ran tests I realised that we had errors saying that the buildables could not be built even though the player had the right materials. I eventually figured out that it was bc the recipe was never set so when getBuildables was called it always returned an empty list. I had to locate where the game was built and then added this line
        game.getPlayer().setRecipe(new Recipe(new BowFactory(config), new ShieldFactory(config)));
    into GameBuilder.java so that after the player exists, it created their new recipes for the buildables. 



[Merge Request 3](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/6)

[Briefly explain what you did]
    Removed/refactored deprecated methods in Entity called translate. Deprecated methods mean that the method has been superseded by a newer and more efficient approach but it still works for now. The comment above the void translate method that takes in the offset tells us to use setPosition instead. In this MR i will replace calls of translate with setPosition. These changes were made in Bomb's onPutDown and GameMap's MoveTo. 


[Merge Request 4](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/7)

[Briefly explain what you did]
    As mentioned before, entity factory was a bit like a god-class, violating SRP as it is responsible for creating entities and knowing about every single entity type, and also OCP because to add a new entity, you have to modify the switch statement and the file.

    I wanted to fix this by implementing a factory method. 
    Factory methods typically had the following structure:
    - product - declares the interface common to all products - already exists via Entity.java
    - concrete products --> already existed (Player.java, ZombieToast.java etc)
    - creator class declares factory method that returns new product objects--> needed to make (will call EntityCreator.java)
    concrete creators - overrides the base factory method  so it returns a different type of product --> needed to make
    
    This meant that i then needed to refactor entityfactory to act as the registry - used to choose which concrete creator it should use. 

    Since all the creators use pos, i added in a getPosition in entityCreator so the same line of code wasnt constantly in a bunch of files.

    For spawnSpider and spawnZombie i needed to modify them to use the creators. In EntityFactory, since it was becoming a registry for all creators, i created a hash map of entity types to replace the big switch statement that manually created each entity. Then the entityfactory class registered all these creators, and added them to the creators map in registerCreato. This allows createEntity to look up the right creator dynamically by type. These entities were then created in createEntity. I decided to keep spawn logic in entityfactory since the factory decides when and where to place spiders and zombies in the world and the logic for spawning a spider and zombie is universal for all its types.

[Merge Request 5](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/8)
[Briefly explain what you did]
    Law of Demeter states that an object should only talk to its immediate friends.
    This is violated in BattleFacade in Battle when setting and retrieving the entities from the map (game.getMap().getEntities) and the health of the player and enemy  - player.getBattleStatistics().setHealth(playerBattleStatistics.getHealth()). 

    I added a method in game.java to get all of the allied mercenaries (getAlliedMercenaries). i refactored a lot of "1. " in battle because it was overreaching a lot to get the player's inventory. I added a getBattleItems method to player, and then in inventory as well whihc returned a list of the player's items that could be used in battle.

    Then to fix the violations that occured when battlefacade reached into player and enemy to retrieve and set their health, i just made health getters and setters in enemy and player.

[Merge Request 6](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/9)
[Briefly explain what you did]
    I corrected Law of Demeter violations in Game.java by implementing my use of getHealth() methods in player and enemy. 

[Merge Request 7](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/10)
[Briefly explain what you did]
    GameMap.java has some violations of SRP because its responsible for the initialisation of the gamemap and then also all the current movements and responsibilities of the gamemap (e.g., entity movements, etc).

    Resolved by extracting the initiationalisation responsibilities out of GameMap into MapInitialiser.java. I made helper functions getEntities to return all entities and then entities of a specific type, depending on what's parsed in the function.

    I then needed to make fixes in Game.java (change the initialisation to occur in MapInitialiser), in EntityFactory (gameMap no longer had a registerPotionListener method so i just registered it directly with the player instead)


## Task 2) Evolution of Requirements 🔧

### Sun Stone & More Buildables (20 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/11
https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/12
https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/13
https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5592426/assignment-ii/-/merge_requests/14
)

**Assumptions**

[Any assumptions made]

**Design**

[Design]
    SunStone --> Made SunStone extend Treasure since its a special form of treasure
    Sceptre --> builable when implements Usable. Item is removed after durability (set num) is 0 bc then the game would be too OP - so durability = mind_control_duration. 

    Sceptre:
        - Make Sceptre.java
        - Make sceptre factory
        - add recipe and edit constructor and added recipe to game builder
        - add functionality to mercenary
        - changed it so scepture was immediately "used"/removed by the player

    armour:
        - make MidnightArmour.java
        - make armour factory
        - add receipe and edit constructor
        - add recipe to game builder
        - ensure buffs of armour are applied

**Changes after review**

[Design review/Changes made]
    Sunstone:
        - Make product creator (SunStoneCreator) and added to Entity Factory
        - Changed pickUp method in Player.java to ensure SunStone counts towards treasure goal
        - Edit door logic so sunStones could also be used as keys for doors in Door.java (edited hasKey method). Also edited onOverlap method in Door.java to ensure sunstones were not consumed upon opening a door.
        - excluded sunstones from bribery in Mercenary.java. Edited canBeBribed to exclude sunStones and then made method useBribeTreasure (in Player.java) to pull only treasure (not sunStones) so it could be used in bribe() to ensure sunStones are not used in bribing mercenaries. 
        - Crafting logic - ensured sunStones could be used as a replacement for keys/treasure in crafting but is not removed from inventory after replacement.

    Sceptre:
        - Make Sceptre.java
        - Make sceptre factory
        - add recipe and edit constructor and added recipe to game builder
        - add functionality to mercenary --> canBeBribed i had to play around with a bit becuase i originally had it make the mercenary become an ally no matter the condition but then as a safety check i moved it into each if statement in case sceptre was null. isInteractable i changed so that it also checked for a sceptre. I also changed the functionality of move() a lot to try determine the best way to implement it so that it matched the described tick behaviour in the spec. 

        - changed it so scepture was immediately "used"/removed by the player

    armour:
        - make MidnightArmour.java
        - make armour factory
        - add receipe and edit constructor. WHen doing this i had some issues trying to determine how to check if zombies currently existed in the game since recipe didnt take in the game. Since recipe was the determinent class for seeing if smoething could be crafted or not, i decided to add GameMap to the constructor of recipe. then i could add a function in recipe to check if zombies existed in the map --> explore if better design 
        - add recipe to game builder
        - ensure buffs of armour are applied

**Test list**

[Test List]
    - Make a sunstone correctly and add to ivnentory 
    - sunstone used in treasure goal
    - sunstone used to open door and not deleted
    - only treasure in inventory is sunstone - check cant be used for bribing mercenaries.
    - mixed treasures - check not able to use in bribing mercenaries
    - used instead to craft shield and not deleted

    sceptre
    - craft a scepre and add to inventory
    - mindcontrol working completely - bro idek if its working

    armour
    - craft the armour given no zombies and add to inventory
    - cannot craft armour if there are zombies and doesnt add to inventory
    - armour adds buffs (Midnight armour provides extra attack damage as well as protection, and it lasts forever.) - cant test bc we cant access the methods that look at health

**Other notes**

[Any other notes]
