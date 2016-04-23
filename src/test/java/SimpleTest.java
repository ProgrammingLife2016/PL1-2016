import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Simple test class to test if JUnit works.
 */
public class SimpleTest {
    /**
     * Assert that true == true.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void test() {
        assertTrue(0 == -0);
    }
}
