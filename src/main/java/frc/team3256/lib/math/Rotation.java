package frc.team3256.lib.math;

/**
 * Class representing a 2D rotation matrix (SO2) [[cos(theta), -sin(theta)] [sin(theta),
 * cos(theta)]]
 */
public class Rotation {

  private double kEpsilon = 1E-9;
  private double cos;
  private double sin;

  /**
   * Create a rotation matrix from the sin and cos of the angle
   *
   * @param cos cos of the angle of the rotation
   * @param sin sin of the angle of the rotation
   * @param normalize whether or not we should "normalize" this rotation {@link #normalize()}
   */
  public Rotation(double cos, double sin, boolean normalize) {
    this.cos = cos < kEpsilon ? 0.0 : cos;
    this.sin = sin < kEpsilon ? 0.0 : sin;
    if (normalize) normalize();
  }

  /** Default constructor: rotation matrix of angle 0 */
  public Rotation() {
    this(1.0, 0.0, false);
  }

  /**
   * Create a rotation matrix from a translation vector This basically is the angle from the
   * positive x axis to the translation vector
   *
   * @param directionVector the translation vector to calculate the angle from
   */
  public Rotation(Translation directionVector) {
    this(directionVector.getX(), directionVector.getY(), true);
  }

  /** Normalizes the rotatation matrix by forcing the sin and cos values to be in the unit circle */
  private void normalize() {
    double magnitude = Math.hypot(cos, sin);
    if (magnitude > kEpsilon) {
      cos /= magnitude;
      sin /= magnitude;
    } else {
      cos = 1.0;
      sin = 0.0;
    }
  }

  public double getCos() {
    return cos;
  }

  public double getSin() {
    return sin;
  }
}
