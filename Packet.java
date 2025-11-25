import java.io.Serializable;

/**
 * Stores information to be sent over Output
 * and InputStreams between the server and client(s).
 * Contains an informational header (type) and a
 * payload (objects).
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 24, 2025
 */

public class Packet implements Serializable, PacketInterface {
    private Object[] objects;
    private PacketType type;

    public Packet(PacketType type) {
        objects = null;
        this.type = type;
    }

    public Packet(PacketType type, Object[] objects) {
        this.objects = objects;
        this.type = type;
    }

    public Object[] getObj() {
        return objects;
    }

    public PacketType getType() {
        return type;
    }
}
