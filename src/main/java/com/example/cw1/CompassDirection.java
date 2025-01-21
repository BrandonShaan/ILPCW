package com.example.cw1;



public enum CompassDirection {
    EAST(0), EAST_NORTH_EAST(22), NORTH_EAST(45), NORTH_NORTH_EAST(67),
    NORTH(90), NORTH_NORTH_WEST(112), NORTH_WEST(135), WEST_NORTH_WEST(157),
    WEST(180), WEST_SOUTH_WEST(202), SOUTH_WEST(225), SOUTH_SOUTH_WEST(247),
    SOUTH(270), SOUTH_SOUTH_EAST(292), SOUTH_EAST(315), EAST_SOUTH_EAST(337);

    private final int angle;

    CompassDirection(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static CompassDirection fromAngle(double angle) {

        angle = (angle + 360) % 360;

        CompassDirection closestDirection = EAST;
        double minDifference = 360;

        for (CompassDirection direction : values()) {
            double diff = Math.abs(direction.angle - angle);
            if (diff < minDifference) {
                minDifference = diff;
                closestDirection = direction;
            }
        }

        return closestDirection;
    }
}
