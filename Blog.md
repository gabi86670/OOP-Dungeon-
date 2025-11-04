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

[Links to your merge requests](/put/links/here)

> i. List one design principle that is violated by collectable objects based on the description above. Briefly justify your answer.

[Answer]
    SRP (single responsibility principle) is violated as instead on focusing on one functionality/responsibility, collectable items like wood and treasure have responsibilities/functionalities for unrelated methods that do things like track durabality and apply buffs. This makes the classes harder to maintain.


> ii. Refactor the inheritance structure of the code, and in the process remove the design principle violation you identified.

[Briefly explain what you did]
    I implemented the refactoring method 'Extract Class' where i made classes Buff.java and Durability.java. Then in InventoryItem.java, the abstract methods for durability and buff become public methods that returned the appropriate results depending if a collectable item has durability or buff-ness or not. If a collectable did have a durability/buff effect, then it was found and returned using the Buff.java and Durability.java classes. Then I could delete instances of these abstract methods from collectables that didnt care about durability and buff effects (i.e., in Wood.java, Key.java, Arrow.java, Bomb.java and Treasure.java). 
    In collectable items that did have durability and buff effects, the durability and buff of that item was set in its constructor where it made new instances of objects of Buff.java and Durability.java. The changes made to these fields were now no longer a concern of these collectables, but rather of their according classes. 

### c) Open-Closed Goals (6 marks)

[Links to your merge requests](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]

### d) Open Refactoring (12 marks)

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:

## Task 2) Evolution of Requirements 🔧

[DELETE ONE OF THESE!]

### Sun Stone & More Buildables (20 marks)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Logic Switches (30 marks)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]
