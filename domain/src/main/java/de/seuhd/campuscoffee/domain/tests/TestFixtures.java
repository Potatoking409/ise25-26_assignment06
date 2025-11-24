package de.seuhd.campuscoffee.domain.tests;

import de.seuhd.campuscoffee.domain.model.*;
import de.seuhd.campuscoffee.domain.ports.PosService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import org.apache.commons.lang3.SerializationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test fixtures for domain objects.
 */
public class TestFixtures {
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2025, 10, 29, 12, 0, 0);


    private static final List<User> USER_LIST = List.of(
            User.builder()
                    .id(1L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .loginName("jane_doe")
                    .emailAddress("jane.doe@uni-heidelberg.de")
                    .firstName("Jane")
                    .lastName("Doe")
                    .build(),
            User.builder()
                    .id(2L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .loginName("maxmustermann")
                    .emailAddress("max.mustermann@campus.de")
                    .firstName("Max")
                    .lastName("Mustermann")
                    .build(),
            User.builder()
                    .id(3L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .loginName("student2023")
                    .emailAddress("student2023@study.org")
                    .firstName("Student")
                    .lastName("Example")
                    .build()
    );

    private static final List<Pos> POS_LIST = List.of(
            Pos.builder()
                    .id(1L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .name("Schmelzpunkt").description("Great waffles")
                    .type(PosType.CAFE).campus(CampusType.ALTSTADT)
                    .street("Hauptstraße").houseNumber("90").postalCode(69117).city("Heidelberg")
                    .build(),
            Pos.builder()
                    .id(1L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .name("Bäcker Görtz ").description("Walking distance to lecture hall")
                    .type(PosType.BAKERY).campus(CampusType.INF)
                    .street("Berliner Str.").houseNumber("43").postalCode(69120).city("Heidelberg")
                    .build(),
            Pos.builder()
                    .id(1L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .name("Café Botanik").description("Outdoor seating available")
                    .type(PosType.CAFETERIA).campus(CampusType.INF)
                    .street("Im Neuenheimer Feld").houseNumber("304").postalCode(69120).city("Heidelberg")
                    .build(),
            Pos.builder()
                    .id(1L).createdAt(DATE_TIME).updatedAt(DATE_TIME)
                    .name("New Vending Machine").description("Use only in case of emergencies")
                    .type(PosType.VENDING_MACHINE).campus(CampusType.BERGHEIM)
                    .street("Teststraße").houseNumber("99a").postalCode(12345).city("Other City")
                    .build()
    );

    public static List<User> getUserList() {
        return USER_LIST.stream()
                .map(SerializationUtils::clone)
                .toList();
    }

    public static List<User> getUserListForInsertion() {
        return getUserList().stream()
                .map(user -> user.toBuilder().id(null).createdAt(null).updatedAt(null).build())
                .toList();
    }

    public static List<Pos> getPosList() {
        return POS_LIST.stream()
                .map(SerializationUtils::clone) // prevent issues when tests modify the fixture objects
                .toList();
    }

    public static List<Pos> getPosFixturesForInsertion() {
        return getPosList().stream()
                .map(user -> user.toBuilder().id(null).createdAt(null).updatedAt(null).build())
                .toList();
    }

    public static List<User> createUsers(UserService userService) {
        return getUserListForInsertion().stream()
                .map(userService::upsert)
                .collect(Collectors.toList());
    }

    public static List<Pos> createPosFixtures(PosService posService) {
        return getPosFixturesForInsertion().stream()
                .map(posService::upsert)
                .collect(Collectors.toList());
    }
}
