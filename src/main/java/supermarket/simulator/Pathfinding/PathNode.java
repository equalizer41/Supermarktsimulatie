package supermarket.simulator.Pathfinding;

/**
 * Represents a node in the A* pathfinding algorithm
 * Contains position, costs, and parent reference for path reconstruction
 */
public class PathNode implements Comparable<PathNode> {
    private int x;
    private int y;
    private double gCost; // Cost from start to this node
    private double hCost; // Heuristic cost from this node to goal
    private double fCost; // Total cost (g + h)
    private PathNode parent; // Parent node for path reconstruction

    /**
     * Constructor for PathNode
     * @param x X coordinate
     * @param y Y coordinate
     */
    public PathNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.gCost = Double.MAX_VALUE;
        this.hCost = 0;
        this.fCost = Double.MAX_VALUE;
        this.parent = null;
    }

    /**
     * Calculate and update F cost
     */
    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }

    /**
     * Calculate Manhattan distance heuristic to target
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     */
    public void calculateHeuristic(int targetX, int targetY) {
        this.hCost = Math.abs(x - targetX) + Math.abs(y - targetY);
    }

    /**
     * Calculate Euclidean distance heuristic (optional, more accurate but slower)
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     */
    public void calculateEuclideanHeuristic(int targetX, int targetY) {
        int dx = x - targetX;
        int dy = y - targetY;
        this.hCost = Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int compareTo(PathNode other) {
        // For priority queue - lower F cost has higher priority
        int fCompare = Double.compare(this.fCost, other.fCost);
        if (fCompare == 0) {
            // If F costs are equal, compare H costs (tie-breaker)
            return Double.compare(this.hCost, other.hCost);
        }
        return fCompare;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PathNode pathNode = (PathNode) obj;
        return x == pathNode.x && y == pathNode.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "PathNode[" + x + "," + y + " | g=" + String.format("%.1f", gCost) + 
               " h=" + String.format("%.1f", hCost) + " f=" + String.format("%.1f", fCost) + "]";
    }

    // Getters and Setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getGCost() {
        return gCost;
    }

    public void setGCost(double gCost) {
        this.gCost = gCost;
        calculateFCost();
    }

    public double getHCost() {
        return hCost;
    }

    public void setHCost(double hCost) {
        this.hCost = hCost;
        calculateFCost();
    }

    public double getFCost() {
        return fCost;
    }

    public PathNode getParent() {
        return parent;
    }

    public void setParent(PathNode parent) {
        this.parent = parent;
    }
}
