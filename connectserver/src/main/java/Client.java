public class Client {
    private final Integer index;

    public Client(Integer index) {
        this.index = index;
    }

    public Integer index() {
        return index;
    }

    public static Client create(Integer index) {
        return new Client(index);
    }
}
