package com.hp.imserver.infrastructure.model;

/**
 * @author HP 2023/2/5
 */
public class DummyUserContext implements UserContext{
    @Override
    public Long getUserId() {
        return 66666L;
    }

    @Override
    public String  getUsername() {
        return "dummy_user_66666";
    }
}
