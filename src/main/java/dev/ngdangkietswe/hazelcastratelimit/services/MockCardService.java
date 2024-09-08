package dev.ngdangkietswe.hazelcastratelimit.services;

import dev.ngdangkietswe.hazelcastratelimit.models.CardDto;

import java.util.List;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

public interface MockCardService {

    List<CardDto> mockCards();
}
