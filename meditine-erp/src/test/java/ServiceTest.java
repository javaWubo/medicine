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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-config.xml"})
public class ServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceTest.class);
    @Resource
    private MedicineService medicineService;

    @Test
    public void test1(){
        Medicine md = new Medicine();
        md.setCreateTime(new Date());
        md.setExpirationTime(1);
        md.setMedicineName("阿莫");
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
