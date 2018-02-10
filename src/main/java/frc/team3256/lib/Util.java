package frc.team3256.lib;

public class Util {

    public static double clip (double a, double min, double max) {
        return a > max ? max : (a < min ? min : a);
    }
}
