package com.example.helper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;

public class ObjectMapper {

    private MapperFactory mapperFactory;
    private MapperFacade mapper;

    public ObjectMapper() {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapper = mapperFactory.getMapperFacade();
    }

    public <T, S> S map(T data, Class<S> clazz) {
        return mapper.map(data, clazz);
    }

    public <T, S>List<S> mapAsList(List<T> data, Class<S> clazz) {
        return mapper.mapAsList(data, clazz);
    }

}
