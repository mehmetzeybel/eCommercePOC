package com.turkcell.pasaj;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turkcell.pasaj.dto.PropertyDTO;
import com.turkcell.pasaj.entity.Property;
import com.turkcell.pasaj.repository.PropertyRepository;
import com.turkcell.pasaj.service.PropertyService;
import com.turkcell.pasaj.utils.EncodeDecodeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PropertyRestApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyService propertyService;

    private List<Property> addedPropertList = new ArrayList<>();
    private String keyValueTest ;
    @Before
    public void before() throws Exception {
        keyValueTest = "KeyValueTestStart" + (new Date()).getTime() + "KeyValueTestEnd";
        for(int i=0;i<9;++i){
            Property property = new Property(0L,keyValueTest+i,keyValueTest+i,true,keyValueTest+i,true,keyValueTest+i,true,keyValueTest+i);
            Property addedProperty = propertyService.save(property);
            addedPropertList.add(addedProperty);
        }
    }

    @After
    public void after() throws Exception{
        for(Property addedProperty : addedPropertList){
            propertyRepository.deleteById(addedProperty.getId());
        }
    }

    @Test
    public void When_SaveProperty_Expect_SavedIdIsNotEmpty() throws Exception
    {
        PropertyDTO propertyDTO = new PropertyDTO(0L,"keytest","valuetest",false,"odm value",false,"gp value",false,"description");
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .post("/property/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(propertyDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty()).andReturn();

        PropertyDTO resultDTO = new Gson().fromJson(result.getResponse().getContentAsString(),PropertyDTO.class);
        propertyRepository.deleteById(resultDTO.getId());
    }

    @Test
    public void When_UpdateProperty_Expect_EqualIdWithSaved() throws Exception
    {
        PropertyDTO propertyDTO = new PropertyDTO(0L,"keytest","valuetest",false,"odm value",false,"gp value",false,"description");
        MvcResult result  = mvc.perform( MockMvcRequestBuilders
                .post("/property/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(propertyDTO)))
                .andReturn();
        PropertyDTO resultDTO = new Gson().fromJson(result.getResponse().getContentAsString(),PropertyDTO.class);

        mvc.perform( MockMvcRequestBuilders
                .post("/property/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(resultDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(resultDTO.getId()));

        propertyRepository.deleteById(resultDTO.getId());
    }

    @Test
    public void Whent_FindAllProperties_Expect_FindPropertiesAndNotAnyEmptyId() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/property/findAllProperties")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    public void When_FindAllPropertiesWithPaging_Expect_FindPropertiesAsPageSize() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/property/findAllPropertiesWithPaging")
                .param("page","0")
                .param("size","2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id",hasSize(2)));

    }

    @Test
    public void When_EncryptDecryptPropertyValue_Expect_EncodedValueEqualsWithDecodedValue() throws Exception
    {
        String value = "Test";
        String encodedValue = EncodeDecodeService.getInstance().encodeBase64(value);
        assertEquals(value, EncodeDecodeService.getInstance().decodeBase64(encodedValue));
    }

    @Test
    public void When_EncryptedSearch_Expect_SearchedValueEqualsWithResponseValue() throws Exception{
        for(int i=0;i<9;++i) {
            mvc.perform(MockMvcRequestBuilders
                    .get("/property/searchProperties")
                    .param("search", keyValueTest+i)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].value").value(keyValueTest+i));
        }
    }

    @Test
    public void When_EmptySearchString_Expect_ResultSizeEqualsWithFindAllResultSize() throws Exception{
        MvcResult searchResultWithEmptyString = mvc.perform(MockMvcRequestBuilders
                .get("/property/searchProperties")
                .param("search", "")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult allProperties =  mvc.perform( MockMvcRequestBuilders
                .get("/property/findAllProperties")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        List<PropertyDTO> searchPropertyResult = (new Gson()).fromJson(searchResultWithEmptyString.getResponse().getContentAsString(),new TypeToken<List<PropertyDTO>>(){}.getType());
        List<PropertyDTO> allPropertyResult = (new Gson()).fromJson(allProperties.getResponse().getContentAsString(),new TypeToken<List<PropertyDTO>>(){}.getType());

        assertEquals(searchPropertyResult.size(), allPropertyResult.size());
    }

}
