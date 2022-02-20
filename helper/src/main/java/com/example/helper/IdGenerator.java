package com.example.helper;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;
import java.util.stream.Stream;

public class IdGenerator implements IdentifierGenerator {

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int CHARACTERS_LENGTH = CHARACTERS.length();
    private final int GENERATED_LENGTH = 6;

    private Random random;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj)
        throws HibernateException {
        random = new Random();

        while (true) {
            String id = generateId();
            String identifierName = session.getEntityPersister(obj.getClass().getName(), obj)
                .getIdentifierPropertyName();
            String query = String.format("select %s from %s where %s='%s'", identifierName,
                obj.getClass().getSimpleName(), identifierName, id);
            Stream ids = session.createQuery(query).stream();
            if (ids.count() == 0) {
                return id;
            }
        }
    }

    private String generateId() {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < GENERATED_LENGTH; i++) {
            resultBuilder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS_LENGTH)));
        }
        return resultBuilder.toString();
    }

}
