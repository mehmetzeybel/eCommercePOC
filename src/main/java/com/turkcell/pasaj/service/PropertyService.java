package com.turkcell.pasaj.service;

import com.turkcell.pasaj.constants.Constants;
import com.turkcell.pasaj.entity.Property;
import com.turkcell.pasaj.repository.PropertyRepository;
import com.turkcell.pasaj.utils.EncodeDecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyLogService propertyLogService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository,PropertyLogService propertyLogService){
        this.propertyRepository = propertyRepository;
        this.propertyLogService = propertyLogService;
    }

    /**
     * Save/Update property to db
     * @param property
     * @return
     * @throws Exception
     */
    public Property save(Property property) throws Exception {
        Property propertyResult = null;
        try {
            propertyResult = propertyRepository.save(encryptValues(property));
            propertyLogService.saveAddUpdateLog(property,Constants.OPERATION_RESULT_SUCCESS);
        }catch (Exception e){
            propertyLogService.saveAddUpdateLog(property,Constants.OPERATION_RESULT_FAILED);
            throw e;
        }
        return propertyResult;
    }

    /**
     * Find all values on property db
     * @return
     * @throws Exception
     */
    public List<Property> findAll() throws Exception {
        List<Property> allProperties = null;

        try {
            allProperties = decryptValues(propertyRepository.findAll());
            propertyLogService.saveSearchLog("", Constants.OPERATION_RESULT_SUCCESS);
        }catch (Exception e){
            propertyLogService.saveSearchLog("", Constants.OPERATION_RESULT_FAILED);
            throw e;
        }
        return allProperties;
    }
    /**
     * Find all values on property db with pagin
     * @return
     * @throws Exception
     */
    public List<Property> findAll(Pageable paging) throws Exception {
        List<Property> allProperties = null;

        try {
            allProperties = decryptValues(propertyRepository.findAll(paging).getContent());
            propertyLogService.saveSearchLog("", Constants.OPERATION_RESULT_SUCCESS);
        }catch (Exception e){
            propertyLogService.saveSearchLog("", Constants.OPERATION_RESULT_FAILED);
            throw e;
        }
        return allProperties;
    }

    /**
     * Find elements with search key
     * @param search
     * @return
     * @throws Exception
     */
    public List<Property> findWithValue(String search) throws Exception {

        List<Property> filteredProperties = null;
        try {
            List<Property> allProperties = decryptValues(propertyRepository.findAll());
            filteredProperties = filterValuesWithHash(allProperties,search);
            propertyLogService.saveSearchLog(search, Constants.OPERATION_RESULT_SUCCESS);
        }catch (Exception e){
            propertyLogService.saveSearchLog(search, Constants.OPERATION_RESULT_FAILED);
            throw e;
        }
        return filteredProperties;
    }



    /**
     * Find element with search key and paging
     * @param search
     * @param paging
     * @return
     * @throws Exception
     */
    public List<Property> findWithValue(String search, Pageable paging) throws Exception{

        List<Property> filteredProperties = null;
        try {
            List<Property> allProperties = decryptValues(propertyRepository.findAll(paging).getContent());
            filteredProperties = filterValuesWithHash(allProperties,search);
            propertyLogService.saveSearchLog(search, Constants.OPERATION_RESULT_SUCCESS);
        }catch (Exception e){
            propertyLogService.saveSearchLog(search, Constants.OPERATION_RESULT_FAILED);
            throw e;
        }
        return filteredProperties;
    }

    /**
     * Make encryption for property object values if Property#isValueHash methods is true
     * @param property
     * @return Property object include encrypted values
     * @throws Exception
     */
    private Property encryptValues(Property property) throws Exception {
        Property tmpProperty = property.toBuilder().build();
        if(tmpProperty.isValueHash()){
            tmpProperty.setValue(EncodeDecodeService.getInstance().encodeBase64(tmpProperty.getValue()));
        }
        if(tmpProperty.isGpValueHash()){
            tmpProperty.setGpValue(EncodeDecodeService.getInstance().encodeBase64(tmpProperty.getGpValue()));
        }
        if(tmpProperty.isOdmValueHash()){
            tmpProperty.setOdmValue(EncodeDecodeService.getInstance().encodeBase64(tmpProperty.getOdmValue()));
        }

        return tmpProperty;
    }

    /**
     * Make decryption for values
     * @param propertyList
     * @return
     * @throws Exception
     */
    private List<Property> decryptValues(List<Property> propertyList) throws Exception {

        List<Property> tmpPropertyList = propertyList.stream().map(property -> property.toBuilder().build()).collect(Collectors.toList());

        for(Property property: tmpPropertyList){
            if(property.isValueHash()){
                property.setValue(EncodeDecodeService.getInstance().decodeBase64(property.getValue()));
            }
            if(property.isGpValueHash()){
                property.setGpValue(EncodeDecodeService.getInstance().decodeBase64(property.getGpValue()));
            }
            if(property.isOdmValueHash()){
                property.setOdmValue(EncodeDecodeService.getInstance().decodeBase64(property.getOdmValue()));
            }
        }
        return tmpPropertyList;
    }

    /**
     * Filter all property elements with search key even encrypted values
     * @param allProperties
     * @param search
     * @return filtered property list
     */
    private List<Property> filterValuesWithHash(List<Property> allProperties,String search){
        return allProperties.stream().filter(property -> property.getValue().contains(search)
                || property.getKey().contains(search)
                || property.getOdmValue().contains(search)
                || property.getGpValue().contains(search)
                || property.getDecription().contains(search)
        ).collect(Collectors.toList());
    }
}
