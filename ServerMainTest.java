import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Minimal test for ServerMain to satisfy the requirement
 * of testing constructors and methods (excluding run's real IO).
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 */
public class ServerMainTest {

    /**
     * Test subclass that overrides run() to avoid opening real sockets.
     */
    private static class TestableServerMain extends ServerMain {

        volatile boolean ran = false;

        public TestableServerMain(int port) {
            super(port);
        }

        @Override
        public void run() {
            ran = true;
        }
    }

    @Test(timeout = 1000)
    public void testStartServerStartsThread() throws InterruptedException {
        TestableServerMain s = new TestableServerMain(12345);

        s.startServer();

        // Give the thread a moment to run
        Thread.sleep(100);

        assertTrue("run() should have been invoked on background thread", s.ran);
    }
}
