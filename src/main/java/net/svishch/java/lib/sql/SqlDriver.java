/*
Драйвер для работы с SQL базами.
Для работы требуются библиотеки:
- MSSQL - "net.sourceforge.jtds.jdbc.Driver"  (jtds-1.3.1.jar)
- MYSQL - "com.mysql.jdbc.Driver" (mysql-connector-java-5.1.xx)

<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.48</version>
</dependency>

 */
package net.svishch.java.lib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Драйвер для подключения к MS SQL и MYSQL базам.
 * Date: 02.10.2018
 *
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 */
public class SqlDriver {

    String host = null;
    String port = null;

    
    String baseName = null;
    String login = null;
    String pass = null;

    String JDBC_DRIVER = null;
    
    String prefix = null;
    String postfix = "";

    private SqlDriver(String sqlType) {
        init(sqlType);
    }

    /**
     * Запрос в базу данных 
     * @param query Тело запроса
     * @return Возвращает результат ответа в виде [List]
     * @throws Exception Возвращает ошибку
     */
    public List executeQuery(String query) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        Exception e = null;
        try {  
            Class.forName(JDBC_DRIVER);  
            String url =  prefix + host + ":" + port + "/" + baseName;
            
            System.out.println("url  = " +url);
            conn = DriverManager.getConnection(url, login, pass);
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            return report(rs);

        } catch (Exception ex) {
            e = ex;

        } finally {   
                if (conn != null) {
                    conn.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                 if (rs != null) {
                    rs.close();
                }               
        }
 
        if (null != e) {
            throw new Exception(e.getMessage());
        }
        return null;

    }

    // Упаковка в массив результатов запроса
    private List report(ResultSet rs) throws SQLException {

        List report = new ArrayList();

        // Количество колонок в результирующем запросе
        int columns = rs.getMetaData().getColumnCount();

        ResultSetMetaData rsmd = rs.getMetaData();

        // Перебор строк с данными
        while (rs.next()) {

            Map columnDate = new HashMap();
            for (int i = 1; i <= columns; i++) {
                // Название колонки, данные колонки
                columnDate.put(rsmd.getColumnName(i), rs.getString(i));
            }
            // Добовляем строку с данными
            report.add(columnDate);
        }

        return report;
    }

   

    public static SqlDriver MSSQL = new SqlDriver("MSSQL");
    public static SqlDriver MySQL = new SqlDriver("MySQL");

    private void init(String sqlType) {

        switch (sqlType) {
            case "MSSQL":
                initMSSQL();
                break;
            case "MySQL":
                initMySQL();
                break;
            default:
                initMySQL();
                break;
        }
    }
    private void initMSSQL(){
    JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";  
    prefix = "jdbc:jtds:sqlserver://";   
    port = "1433";   
    }
    
    private void initMySQL(){
    JDBC_DRIVER = "com.mysql.jdbc.Driver";    
    prefix = "jdbc:mysql://"; 
    postfix = "?useUnicode=true&characterEncoding=utf8";
    port = "3306";       
    }

    /**
     * Возвращает хост соединения.
     * @return возвращает [String]
     */
    public String getHost() {
        return host;
    }

    /**
     *  Возвращает порт соединения.
     * @return возвращает [String]
     */
    public String getPort() {
        return port;
    }

    /**
     * Возвращает имя базы.
     * @return возвращает [String]
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * Возвращает логин подключения.
     * @return возвращает [String]
     */
    public String getLogin() {
        return login;
    }

    /**
     * Устанавливает хост соединения.
     * @param host  хост [String]
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Устанавливает порт соединения.
     * @param port  порт [String]
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Устанавливает логин соединения.
     * @param login логин [String]
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Устанавливает пароль соединения.
     * @param pass пароль [String]
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Устанавливает имя базы соединения.
     * @param baseName имя базы [String]
     */
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }
    
    
}
