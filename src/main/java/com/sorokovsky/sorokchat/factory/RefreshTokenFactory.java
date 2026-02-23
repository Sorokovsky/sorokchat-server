package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.TokenModel;
import com.sorokovsky.sorokchat.model.UserModel;

import java.util.function.Function;

public interface RefreshTokenFactory extends Function<UserModel, TokenModel> {
}
