package course.QStack.FactoryPattern.MethonFactory.enums;

import course.QStack.FactoryPattern.MethonFactory.factory.AudiCarFactory;
import course.QStack.FactoryPattern.MethonFactory.factory.MercedesBenzCarFactory;

public enum CarEnum {
    AUDI(1, "audi", AudiCarFactory.class),
    MERCEDES_BENZ(2, "mercedes_benz", MercedesBenzCarFactory.class);

    private int code;
    private String name;
    private Class<?> clazz;

    CarEnum(int code, String name, Class<?> clazz) {
        this.code = code;
        this.name = name;
        this.clazz = clazz;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
