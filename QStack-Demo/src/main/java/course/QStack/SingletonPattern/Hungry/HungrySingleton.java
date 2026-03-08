package course.QStack.SingletonPattern.Hungry;

public class HungrySingleton {
    private static HungrySingleton intense = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return intense;
    }
}
