package course.QStack.ObservePattern.observe;

public class Borrower implements Observe{

    private String name;

    public Borrower(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("Borrower Name: " + name + " Receive Message: " + message);
    }
}
