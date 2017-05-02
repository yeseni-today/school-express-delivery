import com.delivery.Application;
import com.delivery.common.dao.CreditRecordDao;
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

    @Test
    public void a(){
        System.out.println(creditRecordDao.findByUserId(userid).size());
    }
}
