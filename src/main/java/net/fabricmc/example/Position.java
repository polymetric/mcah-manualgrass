package net.fabricmc.example;

public class Position {
    public int x;
    public int y;
    public int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return String.format("%12d %12d %12d", x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        return equals((Position) o);
    }

    public boolean equals(Position o) {
        return x == o.x && y == o.y && z == o.z;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
