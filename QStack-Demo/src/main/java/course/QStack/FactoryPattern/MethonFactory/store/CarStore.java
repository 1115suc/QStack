package course.QStack.FactoryPattern.MethonFactory.store;

import course.QStack.FactoryPattern.MethonFactory.factory.CarFactory;
import course.QStack.FactoryPattern.MethonFactory.service.Car;

public class CarStore {

    private CarFactory carFactory;

    public CarStore(CarFactory carFactory) {
        this.carFactory = carFactory;
    }

    public Car getCar(Integer type) {
        return null;
    }

}
