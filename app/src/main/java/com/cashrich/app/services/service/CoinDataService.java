package com.cashrich.app.services.service;

import com.cashrich.app.model.CoinData;
import com.cashrich.app.model.User;

public interface CoinDataService {

    //    public Mono<CoinData> fetchAndSaveCoinData(User user, String symbol);
    public CoinData fetchAndSaveCoinData(User user, String symbol);

}
