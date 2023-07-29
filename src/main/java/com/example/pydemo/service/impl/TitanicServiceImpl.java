package com.example.pydemo.service.impl;

import com.example.pydemo.dao.TitanicDao;
import com.example.pydemo.model.Titanic;
import com.example.pydemo.service.TitanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitanicServiceImpl implements TitanicService {

    @Autowired
    private TitanicDao titanicDao;


    @Override
    public List<Titanic> findAll() {
        return titanicDao.findAll();
    }
}
