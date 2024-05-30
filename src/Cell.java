// Class representing a cell in the maze
class Cell {
    // Cell attributes
    int newX, newY; // Coordinates of the cell
    int newDist; // Distance from the start cell
    Cell newPrev; // Previous cell in the path

    // Constructor
    public Cell(int newX, int newY, int newDist, Cell newPrev) {
        this.newX = newX;
        this.newY = newY;
        this.newDist = newDist;
        this.newPrev = newPrev;
    }
}
