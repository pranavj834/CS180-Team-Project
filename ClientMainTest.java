import org.junit.Test;

public class ClientMainTest {

    @Test(timeout = 2000)
    public void testMainDoesNotThrow() {
        // Just ensure main can be called without throwing.
        // If no server is running on port 500, it will print an error and exit.
        ClientMain.main(new String[0]);
    }
}
