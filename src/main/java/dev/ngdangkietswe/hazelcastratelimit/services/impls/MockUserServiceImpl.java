package dev.ngdangkietswe.hazelcastratelimit.services.impls;

import com.github.javafaker.Faker;
import dev.ngdangkietswe.hazelcastratelimit.models.dtos.UserDto;
import dev.ngdangkietswe.hazelcastratelimit.services.MockUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@Service
public class MockUserServiceImpl implements MockUserService {

    @Override
    public List<UserDto> mockUsers() {
        return List.of(
                mockUser(),
                mockUser(),
                mockUser(),
                mockUser(),
                mockUser()
        );
    }

    private UserDto mockUser() {
        Faker faker = new Faker();
        return UserDto.newBuilder()
                .setId(UUID.randomUUID())
                .setUsername(faker.name().username())
                .setEmail(faker.name().username().concat("@gmail.com"))
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setPhoneNumber(faker.phoneNumber().phoneNumber())
                .build();
    }
}
