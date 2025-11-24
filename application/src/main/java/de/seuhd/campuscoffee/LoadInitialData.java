package de.seuhd.campuscoffee;

import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserService;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import de.seuhd.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component that loads initial data into the application when running in the "dev" profile.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
class LoadInitialData implements InitializingBean {
    private final PosService posService;
    private final UserService userService;

    @Override
    public void afterPropertiesSet() {

        log.info("Deleting existing data...");
        posService.clear();
        userService.clear();
        log.info("Loading initial data...");
        List<User> users = TestFixtures.createUsers(userService);
        log.info("Created {} users.", users.size());
        List<Pos> posList = TestFixtures.createPosFixtures(posService);
        log.info("Created {} POS.", posList.size());
        log.info("Initial data loaded successfully.");
    }
}