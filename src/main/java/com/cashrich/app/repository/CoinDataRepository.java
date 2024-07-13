package com.cashrich.app.repository;

import com.cashrich.app.model.CoinData;
import com.cashrich.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinDataRepository extends JpaRepository<CoinData, String> {
}
