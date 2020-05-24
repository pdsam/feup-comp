import java.util.Random;

public class MathUtils {
    public static int random(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min) + min;
    }
}
