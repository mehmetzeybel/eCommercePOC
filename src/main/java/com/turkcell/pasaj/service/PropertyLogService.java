package com.turkcell.pasaj.service;

import com.google.gson.Gson;
import com.turkcell.pasaj.constants.Constants;
import com.turkcell.pasaj.entity.LogProperty;
import com.turkcell.pasaj.entity.Property;
import com.turkcell.pasaj.repository.LogPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PropertyLogService {
    private LogPropertyRepository logPropertyRepository;

    @Autowired
    public PropertyLogService(LogPropertyRepository logPropertyRepository) {
        this.logPropertyRepository = logPropertyRepository;
    }

    /**
     * Save/Update db logs to db
     * @param property
     * @param operationResult
     */
    public void saveAddUpdateLog(Property property,String operationResult){
        String operationType = "";
        if(property.getId()>0){
            operationType = Constants.OPERATION_UPDATE;
        }else{
            operationType = Constants.OPERATION_ADD;
        }
        this.logPropertyRepository.save(new LogProperty(0L,Constants.LOG_USER,operationType,new Date(),new Gson().toJson(property),operationResult));
    }

    /**
     * Save search log to db
     * @param searchKey
     * @param operationResult
     */
    public void saveSearchLog(String searchKey,String operationResult){
        this.logPropertyRepository.save(new LogProperty(0L,Constants.LOG_USER,Constants.OPERATION_SEARCH,new Date(),searchKey,operationResult));
    }
}
