package course.QStack.FactoryPattern.MethonFactory.factory;

import course.QStack.FactoryPattern.MethonFactory.service.Car;
import course.QStack.FactoryPattern.MethonFactory.service.impl.MercedesBenzCar;

public class MercedesBenzCarFactory implements CarFactory{
    @Override
    public Car getCar() {
        return new MercedesBenzCar();
    }
}
