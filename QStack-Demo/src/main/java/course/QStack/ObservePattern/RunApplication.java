package course.QStack.ObservePattern;

import course.QStack.ObservePattern.observe.Borrower;
import course.QStack.ObservePattern.subject.SubscriptionSubject;

public class RunApplication {
    public static void main(String[] args) {
        Borrower borrower = new Borrower("张三");
        Borrower borrower1 = new Borrower("李四");
        Borrower borrower2 = new Borrower("王五");
        SubscriptionSubject subject = new SubscriptionSubject();
        subject.attach(borrower);
        subject.attach(borrower1);
        subject.attach(borrower2);

        subject.notify("可以取钱了！");
    }
}
