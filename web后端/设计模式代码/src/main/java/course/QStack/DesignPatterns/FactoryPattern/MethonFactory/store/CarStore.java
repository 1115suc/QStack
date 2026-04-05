package course.QStack.DesignPatterns.FactoryPattern.MethonFactory.store;

import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.factory.CarFactory;
import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.Car;

public class CarStore {

    private CarFactory carFactory;

    public CarStore(CarFactory carFactory) {
        this.carFactory = carFactory;
    }

    public Car getCar(Integer type) {
        return null;
    }

}
