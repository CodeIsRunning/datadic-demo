package com.lxf.datadic.mapper;

import com.lxf.datadic.dto.SqlRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @program: datadic-demo
 * @description:
 * @author: lxf
 * @create: 2019-11-27 14:20
 **/
@Mapper
public interface DocMapper {

    public List<SqlRes> mysqlDoc(Map<String,String> map);

    public List<String> mysqlTables(Map map);

    public List<SqlRes> oracleDoc(Map<String,String> map);

    public List<String> oracleTables();

}
