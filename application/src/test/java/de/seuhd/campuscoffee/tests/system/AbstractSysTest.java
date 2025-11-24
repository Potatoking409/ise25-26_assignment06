package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.api.mapper.PosDtoMapper;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.ports.PosService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.configurePostgresContainers;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.getPostgresContainer;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Abstract base class for system tests.
 * Sets up the Spring Boot test context, manages the PostgreSQL testcontainer, and configures REST Assured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSysTest {
    protected static final PostgreSQLContainer<?> postgresContainer;

    static {
        // share the same testcontainers instance across all system tests
        postgresContainer = getPostgresContainer();
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        configurePostgresContainers(registry, postgresContainer);
    }

    @Autowired
    protected PosService posService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PosDtoMapper posDtoMapper;


    @Autowired
    protected UserDtoMapper userDtoMapper;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void beforeEach() {
        posService.clear();

        userService.clear();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void afterEach() {
        posService.clear();
    }

    /**
     * Asserts that two objects are equal, ignoring specified fields.
     *
     * @param actual         the actual object
     * @param expected       the expected object
     * @param fieldsToIgnore fields to ignore during comparison
     * @param <T>            the type of the objects being compared
     */
    protected <T> void assertEqualsIgnoringFields(T actual, T expected, String... fieldsToIgnore) {
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(fieldsToIgnore)
                .isEqualTo(expected);
    }

    /**
     * Asserts that two objects are equal, ignoring timestamp fields (createdAt, updatedAt).
     *
     * @param actual   the actual object
     * @param expected the expected object
     * @param <T>      the type of the objects being compared
     */
    protected <T> void assertEqualsIgnoringTimestamps(T actual, T expected) {
        assertEqualsIgnoringFields(actual, expected, "createdAt", "updatedAt");
    }

    /**
     * Asserts that two objects are equal, ignoring ID and timestamp fields.
     *
     * @param actual   the actual object
     * @param expected the expected object
     * @param <T>      the type of the objects being compared
     */
    protected <T> void assertEqualsIgnoringIdAndTimestamps(T actual, T expected) {
        assertEqualsIgnoringFields(actual, expected, "id", "createdAt", "updatedAt");
    }

    /**
     * Asserts that two collections contain the same elements (in any order), ignoring specified fields.
     *
     * @param actual         the actual collection
     * @param expected       the expected collection
     * @param fieldsToIgnore fields to ignore during comparison
     * @param <T>            the type of elements in the collections
     */
    protected <T> void assertEqualsIgnoringFields(List<T> actual, List<T> expected, String... fieldsToIgnore) {
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    /**
     * Asserts that two collections contain the same elements (in any order), ignoring timestamp fields for
     * each element comparison.
     *
     * @param actual   the actual collection
     * @param expected the expected collection
     * @param <T>      the type of elements in the collections
     */
    protected <T> void assertEqualsIgnoringTimestamps(List<T> actual, List<T> expected) {
        assertEqualsIgnoringFields(actual, expected, "createdAt", "updatedAt");
    }
}