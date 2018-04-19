package tdshooter.game;

enum FlightType
{
    LOW_F_SIN(1), HIGH_F_SIN(2), STRAIGHT_DOWN(3), DIAGONAL_45_LEFT(4), DIAGONAL_45_RIGHT(5), WIDE_LOW_F_SIN(6), WIDE_HIGH_F_SIN(7), EXTRAWIDE_SLOW_F_SIN(8);

    private int value;

    private FlightType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static FlightType getByValue(int value) {
        for (FlightType flightType : FlightType.values()) {
            if (flightType.getValue() == value) {
                return flightType;
            }
        }
        return null;
    }
}

public class FlightPatternBuilder
{
    static public FlightPattern create(FlightType flightType, Collidable parent)
    {
        FlightPattern pattern;
        switch(flightType)
        {
            case LOW_F_SIN:
                pattern = new SinCurveFlight(parent, 30, 1.0f);
                break;

            case HIGH_F_SIN:
                pattern = new SinCurveFlight(parent, 30, 0.5f);
                break;

            case STRAIGHT_DOWN:
                pattern = new StraightFlight(parent, 0);
                break;

            case DIAGONAL_45_LEFT:
                pattern = new StraightFlight(parent, 35);
                break;

            case DIAGONAL_45_RIGHT:
                pattern = new StraightFlight(parent, -35);
                break;

            case WIDE_LOW_F_SIN:
                pattern = new SinCurveFlight(parent, 100, 1.0f);
                break;

            case WIDE_HIGH_F_SIN:
                pattern = new SinCurveFlight(parent, 100, 0.5f);
                break;

            case EXTRAWIDE_SLOW_F_SIN:
                pattern = new SinCurveFlight(parent, 300, 0.5f);
                break;

            default:
                pattern = null;
                break;
        }
        return pattern;
    }
}
