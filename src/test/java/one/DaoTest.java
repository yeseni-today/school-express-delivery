package one;

import com.delivery.Application;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.ReviewDao;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.ReviewEntity;
import com.delivery.common.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
    UserDao userDao;

    @Autowired
    ReviewDao reviewDao;

    @Test
    @Rollback(false)
    public void aa() {
//        System.out.println(orderDao.findById("20170423100000").getRecipient().getUid());
//        System.out.println(orderDao.findByState(State.WAIT_ACCEPT));

//        OrderEntity order = orderDao.findById("100001");
//        System.out.println(order.getRecipient());
//        order.getRecipient().setName("ti122");
//        orderDao.casadeUpdate(order);
//        UserEntity user = userDao.findById("10001");
//        System.out.println(user.getName());

        ReviewEntity review = reviewDao.findById("2017051100000");
        System.out.println(review.getUser());
        review.getUser().setName("AliPAy");
        reviewDao.casadeUpdate(review);
    }
//    @Test
    public void a(){
        System.out.println(creditRecordDao.findByUserId(userid).size());
    }
}
