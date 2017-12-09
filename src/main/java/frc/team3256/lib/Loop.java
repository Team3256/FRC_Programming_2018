package frc.team3256.lib;

/** Interface representing a control loop */
public interface Loop {

  void init(double timestamp);

  void update(double timestamp);

  void end(double timestamp);
}
