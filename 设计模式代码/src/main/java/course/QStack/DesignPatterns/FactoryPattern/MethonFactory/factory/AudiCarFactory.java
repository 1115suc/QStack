package course.QStack.DesignPatterns.FactoryPattern.MethonFactory.factory;

import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.Car;
import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.impl.AudiCar;

public class AudiCarFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new AudiCar();
    }
}
