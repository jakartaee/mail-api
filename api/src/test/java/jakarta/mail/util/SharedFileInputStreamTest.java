package jakarta.mail.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class SharedFileInputStreamTest {

    @Test
    public void testGrandChild() throws Exception {
        File file = File.createTempFile("test", "test");

        try (InputStream grandChild = ((SharedFileInputStream) new SharedFileInputStream(file).newStream(0, -1)).newStream(0, -1)) {
            System.gc();
            grandChild.read();
        } catch (IOException e) {
            fail("IOException is not expected");
        }
    }
}