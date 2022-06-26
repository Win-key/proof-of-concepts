package com.winkey.practice.palindservice.service;

import com.winkey.practice.palindservice.dto.ErrorID;
import com.winkey.practice.palindservice.dto.PageResult;
import com.winkey.practice.palindservice.model.PalindromeEntity;
import com.winkey.practice.palindservice.repository.DataRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class AppServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private AppService appService;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(appService, "defaultCount", 30);
    }

    @Test
    public void getPageResult() {
        PalindromeEntity palindromeEntity = new PalindromeEntity("Malayalam");
        Page<PalindromeEntity> pageMock = new PageImpl<>(Collections.singletonList(palindromeEntity));

        Mockito.when(dataRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageMock);

        PageResult pageResult = appService.getPageResult(1,10);
        Assert.assertNotNull(pageResult);
        Assert.assertNotNull(pageResult.getDataList());
        Assert.assertTrue(pageResult.getDataList().size() > 0);
        Assert.assertEquals(pageResult.getDataList().get(0),"Malayalam");
    }

    @Test
    public void putData() {
        PalindromeEntity palindromeEntity = new PalindromeEntity("Malayalam");
        Mockito.when(dataRepository.findByData(Mockito.anyString())).thenReturn(null);

        PalindromeEntity palindromeEntity2 = new PalindromeEntity("Mom");
        Mockito.when(dataRepository.save(Mockito.any(PalindromeEntity.class)))
                .thenReturn(palindromeEntity);

        Page<PalindromeEntity> pageMock = new PageImpl<>(Arrays.asList(palindromeEntity, palindromeEntity2));

        Mockito.when(dataRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageMock);

        // Case insensitive palindrome
        PageResult pageResult = appService.putData("Mom");

        Assert.assertNotNull(pageResult);
        Assert.assertNotNull(pageResult.getDataList());
        Assert.assertTrue(pageResult.getDataList().size() > 0);
        Assert.assertEquals(pageResult.getDataList().get(1),"Mom");

    }

    @Test
    public void putDataNotAPalindromeText() {
        Mockito.when(dataRepository.findByData(Mockito.anyString())).thenReturn(null);

        PageResult pageResult = appService.putData("jiljankjak");

        Assert.assertNotNull(pageResult);
        Assert.assertNull(pageResult.getDataList());
        Assert.assertEquals(ErrorID.NOT_A_PALINDROME, pageResult.getErrorID());

    }

}