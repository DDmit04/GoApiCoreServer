package com.goapi.goapi;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UrlUtils {

    public static String addQueryParamsToUrl(String template, Map<String, String> queryParams) {
        Map<String, List<String>> collect = queryParams.entrySet().stream()
            .collect(Collectors.toMap(e -> e.getKey(), e -> {
                List<String> newVal = new ArrayList<>();
                newVal.add(e.getValue());
                return newVal;
            }));
        MultiValueMap<String, String> queryParamsMap = CollectionUtils.toMultiValueMap(collect);
        String finalUrl = UriComponentsBuilder
            .fromUriString(template)
            .queryParams(queryParamsMap)
            .build()
            .toString();
        return finalUrl;
    }

}
