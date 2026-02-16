package supermarket.simulator.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Person {
    protected int x;
    protected int y;
    protected Color color;
    private String name;
    private int age;

    public Person(int x, int y, Color color) {
        this.name = "Onbekend";
        this.age = 0;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public abstract void update(Grid grid);

    public Color getColor() {
        return color;
    }

    public void draw(GraphicsContext gc, int cellSize) {
        gc.setFill(color);
        gc.fillOval(x * cellSize, y * cellSize, cellSize, cellSize);
    }
}
