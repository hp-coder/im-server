package com.hp.imserver.infrastructure.service;

import com.hp.imserver.infrastructure.model.DummyUserContext;
import com.hp.imserver.infrastructure.model.UserContext;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2023/2/5
 */
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public Optional<UserContext> parseToken(String token) throws IllegalAccessException {
        if (!Objects.equals("666", token)) {
            return Optional.empty();
        }
        return Optional.of(new DummyUserContext());
    }
}
