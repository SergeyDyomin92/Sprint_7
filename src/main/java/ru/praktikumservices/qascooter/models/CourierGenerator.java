package ru.praktikumservices.qascooter.models;

import ru.praktikumservices.qascooter.utils.Utils;

public class CourierGenerator {
    public static Courier randomCourier(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin(utils.login)
                .withPassword(utils.password)
                .withFirstName(utils.firstName);
    }
    public static Courier randomCourierForAuthorization(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin(utils.login)
                .withPassword(utils.password);
    }

    public static Courier randomCourierWithoutLogin(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin("")
                .withPassword(utils.password)
                .withFirstName(utils.firstName);
    }
    public static Courier randomCourierWithoutPassword(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin(utils.login)
                .withPassword("")
                .withFirstName(utils.firstName);
    }
    public static Courier randomCourierForAuthorizationWithoutLogin(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin("")
                .withPassword(utils.password);
    }
    public static Courier randomCourierForAuthorizationWithoutPassword(){
        Utils utils = new Utils();
        return new Courier()
                .withLogin(utils.login)
                .withPassword("");
    }
}
