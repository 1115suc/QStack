package course.QStack.ObservePattern.subject;

import course.QStack.ObservePattern.observe.Observe;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionSubject implements Subject {
    //储存订阅公众号的微信用户
    private List<Observe> borrowers = new ArrayList<Observe>();

    @Override
    public void attach(Observe observer) {
        borrowers.add(observer);
    }

    @Override
    public void detach(Observe observer) {
        borrowers.remove(observer);
    }

    @Override
    public void notify(String message) {
        for (Observe borrower : borrowers) {
            borrower.update(message);
        }
    }
}
