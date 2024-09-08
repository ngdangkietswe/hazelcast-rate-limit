package dev.ngdangkietswe.hazelcastratelimit.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "newBuilder")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CardDto implements Serializable {

    private String cardNumber;
    private String serial;
    private String expireDate;
    private String cardTelco;
}
