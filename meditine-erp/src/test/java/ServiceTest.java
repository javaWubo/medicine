import com.wuch.medicine.domain.Medicine;
import com.wuch.medicine.result.ServiceResult;
import com.wuch.medicine.service.MedicineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: wubo5
 * @create: 2016-08-24 17:31
 * @desc:
 */
public class ServiceTest extends BaseTest {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceTest.class);
    @Resource
    private MedicineService medicineService;

    @Test
    public void test1(){
        Medicine md = new Medicine();
        md.setCreateTime(new Date());
        md.setExpirationTime(new Date());
        md.setExpirationNum(2);
        md.setMedicineName("阿莫ss");
        md.setModifyTime(new Date());
        md.setPrice(200d);
        md.setProducedTime(new Date());
        md.setSaleNum(100);
        md.setStatus(1);
        md.setSurplusNum(20);

       ServiceResult serviceResult = medicineService.addMedicineTx(md);

        LOG.error(serviceResult.toString());

    }

}
