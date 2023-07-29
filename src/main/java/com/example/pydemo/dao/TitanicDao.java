package com.example.pydemo.dao;

import com.example.pydemo.model.Titanic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TitanicDao {
    List<Titanic> findAll();
}
