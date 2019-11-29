package com.lxf.datadic;

import com.lxf.datadic.dto.SqlRes;
import com.lxf.datadic.mapper.DocMapper;
import com.lxf.datadic.utils.SqlTable;
import com.lxf.datadic.utils.WordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DatadicDemoApplicationTests {

    @Resource
    DocMapper docMapper;

    @Test
    void contextLoads() throws Exception {

        List<String> list = new ArrayList<>();
        list.add("CSK");

        //oracle("TJADMIN",list,true);
        mysql("jpa",null,false);
    }




    private void mysql(String dbName,List<String> tables,boolean isTables) throws Exception {

        List<SqlTable> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("dbName", dbName);

        SqlTable mysqlTable = null;
        List<String> tablesList = docMapper.mysqlTables(map);
        if (isTables){
            tablesList=tables;
        }
        for (String table : tablesList
                ) {
            mysqlTable = new SqlTable(table);
            mysqlTable.setTableName(table);

            Map<String, String> map1 = new HashMap();
            map1.put("dbName", dbName);
            map1.put("tableName", table);
            List<SqlRes> result = docMapper.mysqlDoc(map1);
            mysqlTable = Create(table,result);
            list.add(mysqlTable);
        }
        WordUtils wordKit = new WordUtils();
        wordKit.writeTableToWord(list);

    }

    /**
     * oracle 数据字典
     * @param dbName  数据库名
     * @param tables  表名
     * @param isTables 是否使用自定义表名
     * @throws Exception
     */
    private void oracle(String dbName,List<String> tables,boolean isTables) throws Exception {

        List<SqlTable> list = new ArrayList<>();

        SqlTable mysqlTable = null;
        List<String> tablesList = docMapper.oracleTables();
        if (isTables){
            tablesList=tables;
        }
        for (String table : tablesList
                ) {
            mysqlTable = new SqlTable(table);
            mysqlTable.setTableName(table);

            Map<String, String> map1 = new HashMap();
            map1.put("dbName", dbName);
            map1.put("tableName", table);
            List<SqlRes> result = docMapper.oracleDoc(map1);
            mysqlTable = Create(table,result);
            list.add(mysqlTable);
        }
        WordUtils wordKit = new WordUtils();
        wordKit.writeTableToWord(list);

    }


    /**
     *
     * @param tableName 表名
     * @param mysqlRes 表结果
     * @return
     */
    private SqlTable Create(String tableName, List<SqlRes> mysqlRes) {
        SqlTable mysqlTable = new SqlTable(tableName);

        mysqlTable.setCols(5);
        mysqlTable.setRows(mysqlRes.size());
        List<String[]> fieldList = new ArrayList<String[]>();
        for (SqlRes m : mysqlRes
                ) {
            String[] values = new String[5];
            values[0] = m.getColumnName();
            values[1] = m.getColumnType();
            values[2] = m.getColumnDefault();
            values[3] = m.getCharLength();
            values[4] = m.getColumnComment();
            fieldList.add(values);

        }

        mysqlTable.setFieldList(fieldList);

        return mysqlTable;
    }

}
