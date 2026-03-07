package course.QStack.FactoryPattern.MethonFactory.service.impl;

import course.QStack.FactoryPattern.MethonFactory.service.Car;

public class MercedesBenzCar implements Car {
    @Override
    public void run() {
        System.out.println("MercedesBenzCar is running...");
    }

    @Override
    public void stop() {
        System.out.println("MercedesBenzCar is stopping...");
    }
}
