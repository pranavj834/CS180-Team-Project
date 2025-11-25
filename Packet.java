import java.io.Serializable;

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
