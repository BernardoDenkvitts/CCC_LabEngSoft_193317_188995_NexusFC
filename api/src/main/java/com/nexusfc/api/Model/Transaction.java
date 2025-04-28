package com.nexusfc.api.Model;

import com.nexusfc.api.Model.Enum.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Transactions")
public class Transaction {
    @Id
    private String id;

    @Field("amount")
    private BigDecimal amount;

    @Field("status")
    private TransactionStatus status;

    @Field("created_at")
    private Instant createdAt;

    @Field("user_id")
    @DocumentReference
    private User user;
}
