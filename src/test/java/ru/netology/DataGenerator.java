package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("en"));
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendQuery(UserInfo userInfo) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(userInfo) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static class Registration {
        private Registration() {
        }

        public static String generateLogin() {
            return faker.name().username();
        }

        public static String generatePassword() {
            return faker.internet().password();
        }

        public static UserInfo generateValidUser() {
            UserInfo userInfo = new UserInfo(generateLogin(), generatePassword(), "active");
            sendQuery(userInfo);
            return userInfo;
        }

        public static UserInfo generateBlockedUser() {
            UserInfo userInfo = new UserInfo(generateLogin(), generatePassword(), "blocked");
            sendQuery(userInfo);
            return userInfo;
        }

        public static UserInfo generateWrongPasswordUser(String status) {
            String login = generateLogin();
            sendQuery(new UserInfo(login, generatePassword(), status));
            return new UserInfo(login, generatePassword(), status);
        }

        public static UserInfo generateWrongLoginUser(String status) {
            String password = generatePassword();
            sendQuery(new UserInfo(generateLogin(), password, status));
            return new UserInfo(generateLogin(), password, status);
        }
    }
}
