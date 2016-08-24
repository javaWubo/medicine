package com.wuch.medicine.service.impl;

import com.wuch.medicine.dao.MedicineMapper;
import com.wuch.medicine.domain.Medicine;
import com.wuch.medicine.result.ServiceResult;
import com.wuch.medicine.service.MedicineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: wubo5
 * @create: 2016-08-23 21:39
 * @desc:
 */
@Service(value = "medicineService")
public class MedicineServiceImpl implements MedicineService {
    @Resource
    private MedicineMapper medicineMapper;
    @Override
    public ServiceResult addMedicineTx(Medicine medicine) {
        ServiceResult serviceResult = new ServiceResult();
        int retInsert =  medicineMapper.insert(medicine);
        if(retInsert<1){
            throw new RuntimeException("异常");
        }else{
            serviceResult.setCode(ServiceResult.SUCCESS);
            serviceResult.setErrorMsg(ServiceResult.SUCCESS_Msg);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult queryMedicineService() {
        return null;
    }
}
