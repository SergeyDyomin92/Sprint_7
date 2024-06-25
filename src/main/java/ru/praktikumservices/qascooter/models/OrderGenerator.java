package ru.praktikumservices.qascooter.models;

import ru.praktikumservices.qascooter.utils.Utils;


public class OrderGenerator {
    public static Order randomOrder(){
        Utils utils = new Utils();
        return new Order()
                .withFirstName(utils.firstName)
                .withLastName(utils.lastName)
                .withAddress(utils.address)
                .withMetroStation("4")
                .withPhone(utils.phoneNumber)
                .withRentTime(5)
                .withDeliveryDate(utils.deliveryDay())
                .withComment(utils.comment);
    }
    public static Order randomOrderBlack(){
        Utils utils = new Utils();
        return new Order()
                .withFirstName(utils.firstName)
                .withLastName(utils.lastName)
                .withAddress(utils.address)
                .withMetroStation("4")
                .withPhone(utils.phoneNumber)
                .withRentTime(5)
                .withDeliveryDate(utils.deliveryDay())
                .withComment(utils.comment)
                .withColor(new String[]{"BLACK"});
    }
    public static Order randomOrderGray(){
        Utils utils = new Utils();
        return new Order()
                .withFirstName(utils.firstName)
                .withLastName(utils.lastName)
                .withAddress(utils.address)
                .withMetroStation("4")
                .withPhone(utils.phoneNumber)
                .withRentTime(5)
                .withDeliveryDate(utils.deliveryDay())
                .withComment(utils.comment)
                .withColor(new String[]{"GRAY"});
    }
    public static Order randomOrderBlackAndGray(){
        Utils utils = new Utils();
        return new Order()
                .withFirstName(utils.firstName)
                .withLastName(utils.lastName)
                .withAddress(utils.address)
                .withMetroStation("4")
                .withPhone(utils.phoneNumber)
                .withRentTime(5)
                .withDeliveryDate(utils.deliveryDay())
                .withComment(utils.comment)
                .withColor(new String[]{"BLACK","GRAY"});
    }
}
