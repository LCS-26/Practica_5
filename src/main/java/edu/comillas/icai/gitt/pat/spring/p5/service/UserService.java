package edu.comillas.icai.gitt.pat.spring.p5.service;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.repository.TokenRepository;
import edu.comillas.icai.gitt.pat.spring.p5.repository.AppUserRepository;
import edu.comillas.icai.gitt.pat.spring.p5.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * TODO#6
 * Completa los métodos del servicio para que cumplan con el contrato
 * especificado en el interface UserServiceInterface, utilizando
 * los repositorios y entidades creados anteriormente
 */

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private Hashing hashing;

    @Override
    public Token login(String email, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) return null;

        Optional<AppUser> optionalUser = appUserRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return null;

        AppUser appUser = optionalUser.get();

        if (!hashing.compare(appUser.getPassword(), password)) return null;

        Optional<Token> existingToken = tokenRepository.findByAppUser(appUser);
        if (existingToken.isPresent()) return existingToken.get();

        Token token = new Token();
        token.setAppUser(appUser);
        return tokenRepository.save(token);
    }

    @Override
    public AppUser authentication(String tokenId) {
        if (!StringUtils.hasText(tokenId)) return null;

        return tokenRepository.findById(tokenId)
                .map(Token::getAppUser)
                .orElse(null);
    }

    @Override
    public ProfileResponse profile(AppUser appUser) {
        if (appUser == null) return null;

        return new ProfileResponse(
                appUser.getName(),
                appUser.getEmail(),
                appUser.getRole()
        );
    }

    @Override
    public ProfileResponse profile(AppUser appUser, ProfileRequest profile) {
        if (appUser == null || profile == null) return null;

        if (StringUtils.hasText(profile.name())) {
            appUser.setName(profile.name());
        }

        if (profile.role() != null) {
            appUser.setRole(profile.role());
        }

        if (StringUtils.hasText(profile.password())) {
            appUser.setPassword(hashing.hash(profile.password()));
        }

        appUserRepository.save(appUser);

        return profile(appUser);
    }

    @Override
    public ProfileResponse profile(RegisterRequest register) {
        if (appUserRepository.findByEmail(register.email()).isPresent()) {
            return null;
        }

        AppUser newUser = new AppUser();
        newUser.setName(register.name());
        newUser.setEmail(register.email());
        newUser.setRole(register.role());

        newUser.setPassword(hashing.hash(register.password()));

        appUserRepository.save(newUser);

        return profile(newUser);
    }

    @Override
    public void logout(String tokenId) {
        if (!StringUtils.hasText(tokenId)) return;

        tokenRepository.deleteById(tokenId);
    }

    @Override
    public void delete(AppUser appUser) {
        if (appUser == null) return;

        appUserRepository.delete(appUser);
    }
}