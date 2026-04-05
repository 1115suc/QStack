package course.QStack.DesignPatterns.FactoryPattern.MethonFactory;

import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.factory.CarFactory;
import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.factory.MercedesBenzCarFactory;
import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.Car;

public class CarApplication {
    public static void main(String[] args) {
        CarFactory carFactory = new MercedesBenzCarFactory();
        Car car = carFactory.getCar();
        car.run();
    }
}
