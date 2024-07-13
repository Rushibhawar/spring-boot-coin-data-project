package com.cashrich.app.services.serviceImpl;

import com.cashrich.app.model.CoinData;
import com.cashrich.app.model.User;
import com.cashrich.app.repository.CoinDataRepository;
import com.cashrich.app.services.service.CoinDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CoinDataServiceImpl implements CoinDataService {

    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    private  CoinDataRepository coinDataRepository;


    @Value("${coinmarketcap.api.key}")
    private  String apiKey;

    @Value("${coinmarketcap.api.baseUrl}")
    private  String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(CoinDataServiceImpl.class);

    @Override
    public CoinData fetchAndSaveCoinData(User user, String symbols) {
        String apiUrl = baseUrl + "/v1/cryptocurrency/quotes/latest";

        URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("symbol", symbols)
                .build()
                .toUri();

        logger.info("uri : {}",uri);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        String responseData = restTemplate.exchange(requestEntity, String.class).getBody();

        // Save to database
        CoinData coinData = CoinData.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .requestData(symbols)
                .responseData(responseData)
                .timestamp(LocalDateTime.now())
                .build();
        return coinDataRepository.save(coinData);
    }


}
