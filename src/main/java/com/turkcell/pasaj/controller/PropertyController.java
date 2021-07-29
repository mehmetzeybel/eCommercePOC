package com.turkcell.pasaj.controller;

import com.turkcell.pasaj.dto.PropertyDTO;
import com.turkcell.pasaj.entity.Property;
import com.turkcell.pasaj.service.PropertyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/property")
@Api(value = "Pasaj Property Api documentation")
public class PropertyController {

    private PropertyService propertyService;
    private ModelMapper modelMapper;

    @Autowired
    public PropertyController(PropertyService propertyService, ModelMapper modelMapper){
        this.propertyService = propertyService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    @ApiOperation(value = "Save/Update Property method")
    public ResponseEntity<PropertyDTO> save(@RequestBody PropertyDTO propertyDTO) throws Exception {
        Property property = this.propertyService.save(modelMapper.map(propertyDTO, Property.class));
        return ResponseEntity.ok(modelMapper.map(property,PropertyDTO.class));
    }

    @GetMapping("/searchProperties")
    @ApiOperation(value = "Search properties method with search key")
    public ResponseEntity<List<PropertyDTO>> searchProperties(@RequestParam String search) throws Exception {
        if(search.isEmpty()){
            return ResponseEntity.ok().body(propertyService.findAll().stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
        }
        List<Property> properties = propertyService.findWithValue(search);
        return ResponseEntity.ok(properties.stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/searchPropertiesWithPaging")
    @ApiOperation(value = "Search properties method with search key with paging")
    public ResponseEntity<List<PropertyDTO>> searchPropertiesWithPaging(@RequestParam String search,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) throws Exception {

        Pageable paging = PageRequest.of(page, size);
        if(search.isEmpty()){
            return ResponseEntity.ok().body(propertyService.findAll(paging).stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
        }
        List<Property> properties = propertyService.findWithValue(search,paging);
        return ResponseEntity.ok(properties.stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/findAllProperties")
    @ApiOperation(value = "Find all properties method")
    public ResponseEntity<List<PropertyDTO>> findAllProperties() throws Exception {
        List<Property> properties =  propertyService.findAll();
        return ResponseEntity.ok(properties.stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
    }
    @GetMapping("/findAllPropertiesWithPaging")
    @ApiOperation(value = "Find all properties method with paging")
    public ResponseEntity<List<PropertyDTO>> findAllPropertiesWithPaging(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size) throws Exception {
        Pageable paging = PageRequest.of(page, size);
        List<Property> properties =  propertyService.findAll(paging);
        return ResponseEntity.ok(properties.stream().map(property -> modelMapper.map(property,PropertyDTO.class)).collect(Collectors.toList()));
    }
}
