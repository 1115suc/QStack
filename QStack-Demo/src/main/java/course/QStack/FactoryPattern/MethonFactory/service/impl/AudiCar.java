package course.QStack.FactoryPattern.MethonFactory.service.impl;

import course.QStack.FactoryPattern.MethonFactory.service.Car;

public class AudiCar implements Car {
    @Override
    public void run() {
        System.out.println("AudiCar is running");
    }

    @Override
    public void stop() {
        System.out.println("AudiCar is stopping");
    }
}
