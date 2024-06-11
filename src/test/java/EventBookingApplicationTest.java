import jdk.jfr.Enabled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(classes = { EventBookingApplicationTest.class }, properties = "spring.profiles.active:test")
@ActiveProfiles("test")
@Enabled
class EventBookingApplicationTest {

    @Test
    void loadApplicationConfigurationTest() {
        Assertions.assertTrue(true);
    }
}
