package com.example.application.data.service;

import com.example.application.data.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class UsersService extends CrudService<Users, Integer> {

    private UsersRepository repository;

    public UsersService(@Autowired UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UsersRepository getRepository() {
        return repository;
    }

}
