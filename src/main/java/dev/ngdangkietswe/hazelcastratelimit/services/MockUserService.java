package dev.ngdangkietswe.hazelcastratelimit.services;

import dev.ngdangkietswe.hazelcastratelimit.models.dtos.UserDto;

import java.util.List;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

public interface MockUserService {

    List<UserDto> mockUsers();
}
