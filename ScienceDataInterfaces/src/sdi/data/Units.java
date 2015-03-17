package sdi.data;

public class Units {

    private final String name;

    public Units(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Units [name=" + name + "]";
    }
}
