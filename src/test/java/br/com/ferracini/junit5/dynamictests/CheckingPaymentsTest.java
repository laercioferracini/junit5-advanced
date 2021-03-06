package br.com.ferracini.junit5.dynamictests;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * @author lferracini
 * @project = junit5-advanced
 * @since <pre>12/09/2020</pre>
 */
public class CheckingPaymentsTest {

    CustomerService customerService = new CustomerService();
    PaymentRepository paymentRepository = new PaymentRepository();

    @TestFactory
    Stream<DynamicTest> withCombinedStreams() {
        return customerService.getCustomers()
                .stream()
                .flatMap(c -> paymentRepository.findAllPayments(c))
                .map(p -> dynamicTest("valid payment? " + p, () -> validate(p)));
    }

    void validate(Payment payment) {
        //some asserts
    }
}
