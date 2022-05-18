package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        //Запрос
        given() //"дано"
                .spec(requestSpec) //Указывается, какая спецификация используется
                .body(new RegistrationDto( //Передача в теле объекта, который будет преобразован в JSON,
                        user.getLogin(),        //собственно логин,
                        user.getPassword(),     //пароль
                        user.getStatus()))      //и статус.
                .when()                         //"когда"
                .post("/api/system/users") //На какой путь, относительно BaseUri отправляется запрос
                .then()                         //"тогда ожидаем"
                .statusCode(200);               //Код 200, все хорошо
    }

    public static String getRandomLogin() {
        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto getRegisteredUser = getUser(status);
            sendRequest(getRegisteredUser);
            return getRegisteredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
