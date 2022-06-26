package com.winkey.practice.palindservice.service;

import com.winkey.practice.palindservice.dto.ErrorID;
import com.winkey.practice.palindservice.dto.PageResult;
import com.winkey.practice.palindservice.model.PalindromeEntity;
import com.winkey.practice.palindservice.repository.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Venkatesh Rajendran
 */

@Slf4j
@Service
public class AppService {

    @Value("${pageable.page.count:30}")
    private Integer defaultCount;

    @Autowired
    private DataRepository dataRepository;

    /**
     * Gets the list of palindrome text stored in data base
     * @param page
     * @param count
     * @return
     */
    @Transactional(readOnly = true, propagation= Propagation.NOT_SUPPORTED)
    public PageResult getPageResult(Integer page, Integer count) {
        if(Objects.isNull(count) || count < 1){
            count = defaultCount;
        }
        Pageable pageable = PageRequest.of(page, count, Sort.by("data").ascending());
        Page<PalindromeEntity> pageResult = dataRepository.findAll(pageable);

        return new PageResult(pageResult.get().map(PalindromeEntity::getData).collect(Collectors.toList()), pageResult.getTotalElements());
    }


    /**
     * Persist the data
     * @param data
     * @return
     */
    public PageResult putData(String data) {
        PalindromeEntity palindromeEntity = dataRepository.findByData(data);
        if(Objects.nonNull(palindromeEntity)){
            return PageResult.builder()
                    .dataList(getPageResult(0, defaultCount).getDataList())
                    .error("The data you provided does already exist.")
                    .errorID(ErrorID.DATA_EXIST).build();
        }
        if(!checkForPalindrome(data)){
            return PageResult.builder()
                    .dataList(getPageResult(0, defaultCount).getDataList())
                    .error("The data you provided is not a palindrome text.")
                    .errorID(ErrorID.NOT_A_PALINDROME).build();
        }
        palindromeEntity = new PalindromeEntity(data);
        try{
            palindromeEntity = dataRepository.save(palindromeEntity);
        }catch (Exception e){
            log.error("Unable to store the data.", e);
            return PageResult.builder().errorID(ErrorID.OTHER).error("Unable to store the data. Please contact support team!").build();
        }
        return getPageResult(0, defaultCount);
    }

    /**
     * Check if the data is palindrome text
     * @param data
     * @return
     */
    private boolean checkForPalindrome(String data) {
        int length = data.length();
        for (int i = 0, k = length-1; i < data.length(); i++, k--) {
            if(!(""+data.charAt(i)).equalsIgnoreCase(""+data.charAt(k))){
                return false;
            }
        }
        return true;
    }

    /**
     * gets data for auto complete
     * @param data
     * @return
     */
    public PageResult getAutoCompleteResult(String data) {
        List<PalindromeEntity> dataList = dataRepository.findByDataStartsWithIgnoreCase(data, Sort.by("data").ascending());
        if(Objects.isNull(dataList) || dataList.isEmpty())
            return PageResult.builder().dataList(Collections.emptyList()).build();

        return PageResult.builder()
                .dataList(dataList.stream().map(PalindromeEntity::getData)
                .collect(Collectors.toList()))
                .build();
    }
}
