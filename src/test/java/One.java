
import com.delivery.Application;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * @author finderlo
 * @date 22/04/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class One {

    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
   public void sqltest(){
//        System.out.println(
            SQLQuery query =  sessionFactory.getCurrentSession().createSQLQuery("SELECT MAX(user_ID) FROM users");
        for (Object o : query.list()) {
            System.out.println(o);
        }
//        );
    }
}
