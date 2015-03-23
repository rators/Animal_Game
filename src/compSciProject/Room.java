package compSciProject;

import compSciProject.gameTools.elemRandom;
import java.util.Arrays;
import static java.lang.System.arraycopy;
/**
 * Room class holds up to ten occupants including the player!
 * Two arrays are made, one holds the NPCs the other holds the Animals
 * Current version: holds all animals in one array
 *
 * Clean out print statements after Driver proves that all mehods
 * work properly.
 *
 */
public class Room {
    /**
     * Static variable make it easier to use specific string
     * values when modifying the room state.
     */
    public static final String DIRTY = "dirty";
    public static final String CLEAN = "clean";
    public static final String HALF_DIRTY = "half-dirty";

    private  String name;
    private  String description;
    private String state;
    private int population = 0;
    private int doorCount = 0;
    private Creature[] occupants = new Creature[10];
    private Door[] doors = new Door[1];
    private PC player;

    public Room(String name, String description, String state) {
        this.description = description;
        this.state = state;
        this.name = name;
        this.doors = new Door[1];
    }

    /**
     * Add neighbor to Room using a growable array.
     */
    public void addNeighbor(Room roomNew, String posit) {
        if (doorCount == 0) {
            doorCount++;
            this.doors[0] = new Door(roomNew, posit);
            return;
        }
        doorCount++;
        int size = this.doors.length + 1;
        Door[] newDoors = new Door[size];
        arraycopy(this.doors, 0, newDoors, 0, size - 1);
        newDoors[size - 1] = new Door(roomNew, posit);
        this.doors = newDoors;
    }

    /**
     * HI
     * Remove creature algorithm looks through the
     * array and pops out the animal that is being removed
     * Algorithm: Save index of animal that needs to be removed.
     * Once entire POPULATION is looked through.
     * Move population back one for consistency
     * And place last animal in population into
     * index of animal being removed.
     */
    public void removeCreature(Creature animal) {
        if (population > 1) {
            population--;
        } else if (population == 1) {
            occupants[0] = null;
            population = 0;
            return;
        }
        int remIndex = 0;
        for (int i = 0; i < population + 1; i++) {
            if (occupants[i] == null) {
                return;
            } else if (occupants[i].getName().equals(animal.getName())) {
                remIndex = i;
                break;
            }
        }
        occupants[remIndex] = occupants[population];
        occupants[population] = null;
    }
    public void removeCreature(PC pc){
        player = null;
    }
    /**
     * *** Is full and Is Empty status check methods ***
     * Most methods that modify the state of a room or look
     * at the amount of occupants in the room look to these methods to check
     * on whether they can run or whether the user can perform an action at all.
     * They are also used
     */
    public boolean isFull() {
        return (population == 10);
    }

    public boolean isEmpty() {
        return (population == 0);
    }

    /**
     * Game state change means the room is being cleaned by a
     * user in game. The state will change according what the user
     * wants. The restrictions for how the room state changes
     * according to project rules have been implement...
     */
    public String iGameStateChange(String state) {
        if (this.state.equals(Room.HALF_DIRTY)) {
            this.state = state;
            return state;
        } else if (this.state.equals(state)) {
            return("Can't make room: " + state + "er ");
        } else {
            this.state = Room.HALF_DIRTY;
            return state;
        }
    }
    /**
     * Add creature is a fail soft array implementation. I am currently
     * thinking of just making a separate fail soft array class and using
     * that instead of having a while entire method here.
     */
    public void addCreature(Creature creature) {
        if (isFull()) {
            return;
        }
        occupants[population] = creature;
        population++;
        creature.setRoom(this);
    }

    /**
     * Setters and getters for members. Some simpler than others
     */
    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public Door[] getDoors() {
        elemRandom.randRooms(this.doors);
        return this.doors;
    }

    public Door[] userGetDoors() {
        return this.doors;
    }

    public PC getPlayer() {
        return this.player;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPlayer(PC player) {
        //Add an extra population parameter, one for population of NPC and ANIMALS
        //  and the player. Set it separately so that it doesn't affect the animal population
        //   that is used as a sentinel variable called: population.
        //System.out.println("Player " + player.getName() + " has entered the room " + this.name);
        this.player = player;
        //System.out.println("Occupants in Current Room...\n" + this.displayOccupants() + "\n");
    }

    /**
     * Methods below aid in the displaying of information relevant
     * to the room and the action the user is trying to do.
     * Get occupants returns a specific array that will not contain
     * any null space! This allows for clean enhanced loops.
     */
    public Creature[] getOccupants() {
        return Arrays.copyOfRange(occupants, 0, population);
    }
    public String forceInhabitant(int indexOf, String action){
        return occupants[indexOf].react(action);
    }
    public int getPopulation(){
        return player!=null ? population+1 : population;
    }

    //Print out all doors to user. Return a error message if there are none.
    private String displayOccupants() {
        if (isEmpty() && player==null) {
            return name + " is empty";
        }
        String roster = "";
        if(player!=null){
            roster += player.toString() + "\n";
        }
        for (int i = 0; i < population; i++)
            roster += occupants[i].toString() + "\n";
        return roster;
    }
    /**
     * Returns a formatted string will all door information and number
     * in the array to the screen.
     */
    private String displayDoors() {
        String doorDescription = "";
        if (doorCount == 0)
            return doorDescription;
        for (Door x : doors) {
            doorDescription += x.toString() + " \n";
        }
        return doorDescription;
    }

    public int getDoorIndex(String find){
        for(int i=0;i<userGetDoors().length;i++){
            if (userGetDoors()[i].getPosition().equals(find))
                return i;
        }
        return -1;
    }

    /**
     * Sort and search algorithms for later part of assignment
     */
    public void sort(){
        quickSortSelf.sortArray(occupants, population);
    }
    public int search(String key){
        binarySearch searcher = new binarySearch();
        return searcher.startSearch(occupants,key,population);
    }

    /**
     * To string made here. Implicitly calls all
     * toStrings of other classes contained in the program.
     */
    public String toString() {
        if (population > 0)
            return "Room name: " + name + "\nDoors:" + "[" + doorCount + "]\n" + "Description : " + description + "\nOccupants" + "[" + getPopulation() + "]...\n"
                    + displayOccupants() + displayDoors();
        else {
            return name + ": " + description + "\nOccupants" + "[" + getPopulation() + "]" + "\n" +
                    displayDoors();
        }
    }

}