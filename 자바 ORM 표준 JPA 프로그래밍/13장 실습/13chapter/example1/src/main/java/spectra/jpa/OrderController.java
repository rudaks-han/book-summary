package spectra.jpa;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("example1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public void view()
    {
        String orderId = "test";
        Order1 order1 = orderService.findOne(orderId);
        Member1 member1 = order1.getMember1();
        System.out.println(member1.getUsername());
    }

    @GetMapping("/fetchTest")
    public void fetchTest()
    {
        List<Order1> order1 = orderService.fetchTest();

        System.out.println(order1.size());
    }

}
