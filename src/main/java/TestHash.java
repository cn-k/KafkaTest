import org.apache.commons.codec.digest.MurmurHash3;

import java.nio.charset.StandardCharsets;

public class TestHash {
    public static void main(String[] args) {
        byte [] arr = "".getBytes(StandardCharsets.UTF_8);
        System.out.println(MurmurHash3.hash64(arr));

    }
}
