package mtdm.dk;
import java.util.Random;

public class RandomGenerator {
  private static final Random random = new Random();

  public static float getRandom() {
    return (float) random.nextDouble();
  }


}
