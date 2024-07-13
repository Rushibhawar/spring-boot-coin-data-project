package com.cashrich.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coin_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CoinData {

    @Id
    @Column(name = "coin_data_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String requestData;

    @Column(columnDefinition = "TEXT")
    private String responseData;

    private LocalDateTime timestamp;
}