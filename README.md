# Application-Programming-Unit-Battleships-Game
This is an Mobile Application I made for my Application Programming Unit at University - This Mobile Application simulates a game of battleships with a another player (a computer programmed to play battleships) on both easy and hard mode

--- 

### Extra feautres:
  - Improve computer player:
    - Computer player can detect when a cell has been hit and will check multiple directions around it until it finds the one the ship lies upon
    - It will continue on that direction until the ship is sunk. If the ship is still not sunk when it reaches the end of the ship it will continue in the oposite direction from the initial hit
    - When not checking directions the computer will also check the entire grid for hit cells that have not been acted upon and plug them back into the direction algorithm, this is useful for when some ships overlap and the computer hits them both when in the direction algorithm

---

### Content of the repository:
  - **Kotlin** - Provides the main functionality of the game as well as the computer programs made to play Battleships
  - **XML** - Creates the layout and overall design of the pages inside the Application


---


## Running the site
To run the site in it's current state you will need load the project into JetBrains Kotlin Application IDE and use it's simulation functionality to run the Application

<br>


## Application - Main page (Home page)
![image](https://github.com/user-attachments/assets/59f404dc-2b61-40c5-a84a-18229f0ac3bd)


## Application - Game page (Midgame of Battleships)
![image](https://github.com/user-attachments/assets/ccdd9016-a5ea-4297-9f44-326b3dd2cd17)
