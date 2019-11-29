package com.lxf.datadic.dto;

import lombok.Data;

/**
 * @program: datadic-demo
 * @description:
 * @author: lxf
 * @create: 2019-11-27 15:30
 **/
@Data
public class SqlRes {

    String columnName;

    String columnType;

    String isNullAble;

    String charLength;

    String columnDefault;

    String columnComment;



}
