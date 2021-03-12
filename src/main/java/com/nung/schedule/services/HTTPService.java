package com.nung.schedule.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

@Service
public class HTTPService {
    private final RestTemplate restTemplate;
    private final String scheduleURL;
    private final HttpHeaders headers;

    public HTTPService(RestTemplateBuilder restTemplateBuilder,
                       @Value("${schedule.encoding}") String encoding,
                       @Value("${schedule.url}") String URL,
                       @Value("${schedule.content-type}") String contentType) {
        restTemplate = restTemplateBuilder.build();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(encoding)));
        scheduleURL = URL;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
    }

    public String postRequestToURL(String date, String groupName) {
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.put("group", List.of(groupName));
        postParameters.put("sdate", List.of(date));
        postParameters.put("edate", List.of(date));

        return Objects.requireNonNull(restTemplate.postForEntity(scheduleURL, new HttpEntity<>(postParameters, headers), String.class).getBody());
    }

    public String postRequestToURL(String fromDate, String toDate, String groupName) {
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.put("group", List.of(groupName));
        postParameters.put("sdate", List.of(fromDate));
        postParameters.put("edate", List.of(toDate));

        return Objects.requireNonNull(restTemplate.postForEntity(scheduleURL, new HttpEntity<>(postParameters, headers), String.class).getBody());
    }
}