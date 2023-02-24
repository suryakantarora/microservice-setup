package com.cgs.authservice.service;

import com.cgs.authservice.model.dto.TokensEntity;
import com.cgs.authservice.repository.TokensRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokensRedisService {


    @Autowired
    private TokensRedisRepository tokensRedisRepository;

    public TokensEntity save(TokensEntity entity) {
        return tokensRedisRepository.save(entity);
    }


    public Optional<TokensEntity> findById(String id) {
        return tokensRedisRepository.findById(id);
    }

    public Iterable<TokensEntity> findAll() {
        return tokensRedisRepository.findAll();
    }


    public void deleteToken(TokensEntity authToken) {
        tokensRedisRepository.delete(authToken);
    }
}
