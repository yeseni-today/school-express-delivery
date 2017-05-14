package one;

import com.delivery.Application;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.ReviewDao;
import com.delivery.common.entity.ReviewEntity;
import com.delivery.order.OrderState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author finderlo
 * @date 02/05/2017
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DaoTest {

    String userid = "213124128";

    @Autowired
    CreditRecordDao creditRecordDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    ReviewDao reviewDao;

    @Test
    public void aa()
    {
//        System.out.println(orderDao.findById("20170423100000").getRecipient().getId());
        System.out.println(orderDao.findByState(OrderState.WAIT_ACCEPT));
    }
//    @Test
    public void a(){
        System.out.println(creditRecordDao.findByUserId(userid).size());
    }
}
