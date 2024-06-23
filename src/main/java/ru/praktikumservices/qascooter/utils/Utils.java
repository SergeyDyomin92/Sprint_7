package ru.praktikumservices.qascooter.utils;

import com.github.javafaker.Faker;

import java.util.Locale;


public class Utils {
    Faker faker = new Faker((new Locale("ru")));

    public String firstName = faker.name().firstName();
    public String password = String.valueOf(faker.number().numberBetween(79000000000L, 79999999999L));

}
