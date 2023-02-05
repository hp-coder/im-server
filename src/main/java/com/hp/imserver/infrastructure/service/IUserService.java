package com.hp.imserver.infrastructure.service;

import com.hp.imserver.infrastructure.model.UserContext;

import java.util.Optional;

/**
 * @author HP 2023/2/5
 */
public interface IUserService {

    Optional<UserContext> parseToken(String token) throws IllegalAccessException;
}
