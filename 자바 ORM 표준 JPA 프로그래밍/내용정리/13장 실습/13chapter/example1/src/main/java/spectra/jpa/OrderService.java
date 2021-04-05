package spectra.jpa;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final JpaTestRepository jpaTestRepository;

    @Transactional
    public Order1 findOne(String orderId)
    {
        return orderRepository.findOne(orderId);
    }

    @Transactional
    public List<Order1> fetchTest()
    {
        return jpaTestRepository.findOrders();
    }
}
