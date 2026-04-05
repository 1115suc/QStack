package course.QStack.DesignPatterns.FactoryPattern.MethonFactory.factory;

import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.Car;
import course.QStack.DesignPatterns.FactoryPattern.MethonFactory.service.impl.MercedesBenzCar;

public class MercedesBenzCarFactory implements CarFactory{
    @Override
    public Car getCar() {
        return new MercedesBenzCar();
    }
}
