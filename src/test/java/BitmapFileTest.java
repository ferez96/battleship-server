import com.hauduepascal.ferez96.battleship.app.Global;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Random;

public class BitmapFileTest {

    @Test
    public void testWrite() throws Exception {
        Path path = Global.PROJECT_PATH.resolve("map_pool").resolve("map0.map");
        System.setOut(new PrintStream(path.toFile()));

        boolean[][] data = new boolean[50][];
        Random rand = new Random(7);
        for (int i = 0; i < 50; ++i) data[i] = new boolean[50];
        for (int i = 0; i < 50; ++i) {
            for (int j = 0; j < 50; ++j) {
                data[i][j] = false;
            }
        }
        for (int i = 1; i <= 30; ++i) {
            int x = rand.nextInt(2500);
            data[x / 50][x % 50] = true;
        }

        for (int i = 0; i < 50; ++i) {
            for (int j = 0; j < 50; ++j) {
                System.out.print((data[i][j]) ? "#" : ".");
            }
            System.out.println();
        }

    }
}
