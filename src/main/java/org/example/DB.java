package org.example;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class DB {
    //массивы для некоторых типов данных
    final String[] charTypes = {"character varying", "text", "varchar"};
    final String[] intTypes = {"integer", "int4"};
    final String longType = "bigint";
    final String shortType = "smallint";
    final String realType = "real";
    final String doubleType = "double precision";
    final String dateType = "date";
    final String boolType = "boolean";

    //данные для подключения; все устанавливаются при входе в БД.
    private String HOST;
    private String PORT;
    private String DB_NAME;
    private String LOGIN;
    private String PASS;

    //Нужно было для тестирования дома.
    //Пусть останется; этот метод отвечает за подключение к базе данных
    //по IP адресу, порту, логину и паролю.

    public void DB_init(String HOST, String PORT, String DB_NAME, String LOGIN, String PASS) throws SQLException, ClassNotFoundException {
        this.HOST = HOST;
        this.PORT = PORT;
        this.DB_NAME = DB_NAME;
        this.LOGIN = LOGIN;
        this.PASS = PASS;
        getDbConn();
    }

    //Соединение с БД
    private Connection DbConn = null;

    //Получаем соединение с БД, если оно присутствует.
    private Connection getDbConn() throws ClassNotFoundException, SQLException {
        //Ссылка, состоящая из хоста, порта и имени пользователя.
        //Тут я сделал postgres, можно поменять на mySQL
        String connStr = "jdbc:postgresql://"+HOST+":"+PORT+"/"+DB_NAME; //если mySQL, то: jdbc:mysql://"+HOST+":"+PORT+"/"+DB_NAME
        Class.forName("org.postgresql.Driver"); //если mySQL, то: jdbc:mysql://"+HOST+":"+PORT+"/"+DB_NAME

        //Соединение... (Если логин и пароль правильные, должно сработать)
        DbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return DbConn;
    }

    /*
     * Получаем столбцы из выбранной таблицы и записываем их в указанный в качестве аргумента массив.
     * Нужно для ввода данных в таблицу в соответствии со столбцами
     * */
    public void getColumns(String tableName, ArrayList<Column> cols) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM "+tableName;

        Statement statement = getDbConn().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        //получаем информацию о столбцах из нашей таблицы
        ResultSetMetaData metaData = resultSet.getMetaData();
        //получаем количество столбцов таблицы
        int columnCount = metaData.getColumnCount();

        //добавляем столбцы в массив столбцов, которые затем должны пойти в объект класса таблица
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnType = metaData.getColumnTypeName(i);
            cols.add(new Column(columnName, columnType));
        }
    }

    //Сам ввод данных в таблицу.
    //Сканнер нужен, чтобы вводить данные; Объект класса Table - для универсальности
    public void insertTask(Table table, Scanner scanner) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO "+table.name+" (";
        for (int i = 0; i < table.cols.size()-1; i++)
        {
            sql = sql+table.cols.get(i).getName()+", ";
        }
        sql+=table.cols.get(table.cols.size()-1).getName();
        sql+=") VALUES (";
        for (int i = 0; i < table.cols.size()-1; i++)
        {
            sql+="?, ";
        }
        sql+="?)";
        PreparedStatement prSt = getDbConn().prepareStatement(sql);
        prSt.setInt(1, getFreeID(table, table.cols.get(0).getName()));
        for (int i = 1; i < table.cols.size(); i++)
        {
            runFill(prSt, i+1, table.cols.get(i),scanner);
        }
        prSt.executeUpdate();
    }

    //получение первого свободного номера в таблице
    public int getFreeID(Table table, String idCol) throws SQLException, ClassNotFoundException
    {
        //запрос
        String sql = "SELECT MAX("+idCol+") FROM "+table.name;
        //пытаемся соединиться и ввести команду.
        Connection conn = getDbConn();
        Statement statement = conn.createStatement();
        ResultSet res = statement.executeQuery(sql);
        //если есть свободные номера, то возвращаем этот самый номер
        //отсчёт здесь почему-то начинается с нуля.
        if(res.next())
        {
            int maxID = res.getInt(1);
            return maxID;
        }
        //иначе возвращаем 1
        else return 1;
    }

    //заполнение таблицы в соответствии с типом данных
    void runFill(PreparedStatement prSt, int index, Column col, Scanner scanner) throws IllegalArgumentException, SQLException {
        //булево значение, нужно нам, чтобы проверить, добавлено ли значение (проверка нужна лишь
        // когда возможных названий типа данных больше, чем одно)
        boolean isAdded = false;
        //возможных названий больше чем одно
        for (int i = 0; i < charTypes.length; i++)
        {
            //если нашлось совпадение, записываем значение "истина" в булево поле и записываем с клавиатуры
            //строку в таблицу.
            if (col.getType().equalsIgnoreCase(charTypes[i]))
            {
                System.out.println("Введите строку для столбца "+col.getName()+": ");
                prSt.setString(index, scanner.next());
                isAdded = true;
            }
            if (isAdded)
                return;
        }
        for (int i = 0; i < intTypes.length; i++)
        {
            //если нашлось совпадение, записываем значение "истина" в булево поле и записываем с клавиатуры
            //32-разрядное целое число в таблицу.
            if (col.getType().equalsIgnoreCase(intTypes[i]))
            {
                System.out.println("Введите целое число для столбца "+col.getName()+": ");
                prSt.setInt(index, scanner.nextInt());
                isAdded = true;
            }
            if (isAdded)
                return;
        }
        //дальше всё просто - если нашёлся тип данных, записываем данные с клавиатуры в таблицу
        if (col.getType().equalsIgnoreCase(longType))
        {
            System.out.println("Введите целое число для столбца "+col.getName()+": ");
            prSt.setLong(index, scanner.nextLong());
            return;
        }
        if (col.getType().equalsIgnoreCase(shortType))
        {
            System.out.println("Введите целое число для столбца "+col.getName()+": ");
            prSt.setShort(index, scanner.nextShort());
            return;
        }
        if (col.getType().equalsIgnoreCase(realType))
        {
            System.out.println("Введите вещественное число для столбца "+col.getName()+": ");
            prSt.setFloat(index, scanner.nextFloat());
            return;
        }
        if (col.getType().equalsIgnoreCase(doubleType))
        {
            System.out.println("Введите вещественное число для столбца "+col.getName()+": ");
            prSt.setDouble(index, scanner.nextDouble());
            return;
        }
        if (col.getType().equalsIgnoreCase(dateType))
        {
            System.out.println("Введите дату для столбца "+col.getName()+": ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.print("Enter a date (yyyy-MM-dd): ");
            String dateInput = scanner.next();
            Date sqlDate = Date.valueOf(dateInput);
            prSt.setDate(index, sqlDate);
            return;
        }
        if (col.getType().equalsIgnoreCase(boolType))
        {
            System.out.println("Введите булево значение для столбца "+col.getName()+": ");
            prSt.setBoolean(index, scanner.nextBoolean());
        }
    }

    /*
    * Вывод таблицы на экран консоли.
    * Пока что столбцы разделяются табуляцией.
    * */
    public void outputEverythingTask(Table table, String sortby) throws SQLException, ClassNotFoundException {
        //Запрос
        String sql = "SELECT * FROM "+table.name+" ORDER BY "+sortby;

        //Подключение, выполнение команды и получение результата
        Statement statement = getDbConn().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        //Объявление двухмерного массива
        ArrayList<ArrayList<String>> tasks = new ArrayList<>();
        int columnCount = table.cols.size();

        //заполнение массива
        while(resultSet.next())
        {
            ArrayList<String> row = new ArrayList<>();;
            for (Column col: table.cols)
            {
                String columnName = col.getName();
                String columnValue = resultSet.getString(columnName);
                row.add(columnValue != null ? columnValue+"\t" : "\t\t");
            }
            tasks.add(row);
        }

        for (Column col: table.cols)
        {
            System.out.print(col.getName()+"\t");
        }
        System.out.println();

        //Вывод таблицы на консоль
        for (ArrayList<String> row : tasks) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}
