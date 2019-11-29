# datadic-demo

本文参考了cpthack 的 mysql2word-master

基于spring boot 的mysql 和oracle 的数据字典的生成word


不用运行项目，test下有直接可以运行的测试方法，可直接用，用的时候配置一下properties文件数据库的连接

`@SpringBootTest
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
`
