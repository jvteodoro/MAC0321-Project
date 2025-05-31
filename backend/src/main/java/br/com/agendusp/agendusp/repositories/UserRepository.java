package br.com.agendusp.agendusp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByEmail(String email);
}
