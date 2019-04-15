package springframework.test.service;

import com.forever.springframework.test.model.Order;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public interface IOrderService {

    String insertOrder(Order order);
}
