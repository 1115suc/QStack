package course.QStack.FactoryPattern.MethonFactory.factory;

import course.QStack.FactoryPattern.MethonFactory.service.Car;
import course.QStack.FactoryPattern.MethonFactory.service.impl.AudiCar;

public class AudiCarFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new AudiCar();
    }
}
