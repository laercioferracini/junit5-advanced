package br.com.ferracini.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lferracini
 * @project = junit5-advanced
 * @since <pre>12/09/2020</pre>
 */
public class CustomerDAO {

    AtomicLong key = new AtomicLong(1L);
    private Map<Long, Customer> mapCustomers;

    public Customer create(String name) {
        return new Customer(name);
    }

    public long save(Customer customer) {
        this.mapCustomers = new HashMap<>();
        mapCustomers.put(key.get(), customer);
        return key.getAndIncrement();
    }

    public Optional<Customer> fetch(long l) {
        return Optional.ofNullable(mapCustomers.get(l));
    }

    public void delete(long l) {
        if (!mapCustomers.containsKey(l)) throw new IllegalArgumentException("Cannot be deleted by wrong id " + l);
        else mapCustomers.remove(l);
    }
}
