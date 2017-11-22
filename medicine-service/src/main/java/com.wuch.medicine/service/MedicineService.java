package com.wuch.medicine.service;

import com.wuch.medicine.domain.Medicine;
import com.wuch.medicine.result.ServiceResult;

import java.util.List;

/**
 * Created by wubo5 on 2016/8/23.
 */
public interface MedicineService {
    /**
     *
     * @param medicine
     * @return
     */
    ServiceResult addMedicineTx(Medicine medicine);


    ServiceResult queryMedicineService( String ace);
}
