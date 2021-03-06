package br.com.ferracini.junit5.nestedtests;

import br.com.ferracini.customer.Customer;
import br.com.ferracini.customer.CustomerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lferracini
 * @project = junit5-advanced
 * @since <pre>12/09/2020</pre>
 */
@DisplayName("A customer object")
public class DAOTest {
    CustomerDAO dao = new CustomerDAO();

    @Test
    @DisplayName("can be created with the dao")
    void canBeCreated() {
        dao.create("Dante");
    }

    @Nested
    @DisplayName("when created")
    class WhenCreated {
        Customer customer;

        @BeforeEach
        void setup() {
            customer = dao.create("Dante");
        }

        @Test
        @DisplayName("it must be saved to the dao")
        void mustBeSaved() {
            assertEquals(1L, dao.save(customer));
        }

        @Nested
        @DisplayName("after saving a customer")
        class AfterSaving {

            @BeforeEach
            void setup() {
                dao.save(customer);
            }

            @Test
            @DisplayName("it can be fetched from the dao")
            void canBeFetched() {
                assertTrue(dao.fetch(1L).isPresent());
            }

            @Test
            @DisplayName("it cannot be deleted by wrong id")
            void cannotBeDeletedByWrongId() {
                assertThrows(IllegalArgumentException.class, () -> dao.delete(-1L));
            }
        }
    }
}
