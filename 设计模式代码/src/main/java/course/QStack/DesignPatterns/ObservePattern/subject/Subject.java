package course.QStack.DesignPatterns.ObservePattern.subject;

import course.QStack.DesignPatterns.ObservePattern.observe.Borrower;
import course.QStack.DesignPatterns.ObservePattern.observe.Observe;

public interface Subject {
    //增加订阅者
    public void attach(Observe observer);

    //删除订阅者
    public void detach(Observe observer);

    //通知订阅者更新消息
    public void notify(String message);
}