package org.model;

public record Vector2D(int x, int y) {

    public Vector2D add(Vector2D unitVector) {
        return new Vector2D(x + unitVector.x(), y + unitVector.y());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2D vector2d = (Vector2D) other;
        return x() == vector2d.x && y() == vector2d.y;
    }
}
