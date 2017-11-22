import com.wuch.medicine.controller.back.door.utils.SpringContext;
import com.wuch.medicine.service.MedicineService;
import org.junit.Test;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * Created by wubo5 on 2017/11/8.
 */
public class BackDoorTest extends BaseTest {
    @Resource
    private SpringContext springContext;
    @Test
    public void test(){
        String beanName = "com.wuch.medicine.service.impl.medicineService";
        MedicineService medicineService =  (MedicineService)springContext.getStringBean(beanName);
//        medicineService.queryMedicineService();
    }



}
