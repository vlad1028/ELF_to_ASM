import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DataSource {
    private byte[] data;

    public DataSource(String filePath) throws IOException {
        this.data = Files.readAllBytes(Paths.get(filePath));
    }

    private int decodeLSB(byte[] array) {
        int ans = 0;
        for (int i = 0; i < array.length; ++i) {
            int t = Byte.toUnsignedInt(array[i]);
            ans += (t << (8 * i));
        }
        return ans;
    }

    public int getValue(int begin, int size) {
        return decodeLSB(Arrays.copyOfRange(data, begin, begin + size));
    }
}
