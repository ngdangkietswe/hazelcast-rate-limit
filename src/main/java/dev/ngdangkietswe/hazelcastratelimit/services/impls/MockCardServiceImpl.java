package dev.ngdangkietswe.hazelcastratelimit.services.impls;

import dev.ngdangkietswe.hazelcastratelimit.models.CardDto;
import dev.ngdangkietswe.hazelcastratelimit.services.MockCardService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Service
public class MockCardServiceImpl implements MockCardService {

    private static final String[] TELCOS = {"VinaPhone", "Viettel", "MobiFone"};
    private static final String EXPIRE_DATE = "12/24";

    @Override
    public List<CardDto> mockCards() {
        return List.of(
                mockCard(),
                mockCard(),
                mockCard(),
                mockCard(),
                mockCard()
        );
    }

    private CardDto mockCard() {
        return CardDto.newBuilder()
                .setCardNumber(RandomStringUtils.secureStrong().nextAlphanumeric(10))
                .setSerial(RandomStringUtils.secureStrong().nextAlphanumeric(10))
                .setExpireDate(EXPIRE_DATE)
                .setCardTelco(TELCOS[RandomUtils.secureStrong().randomInt(0, TELCOS.length)])
                .build();
    }
}
