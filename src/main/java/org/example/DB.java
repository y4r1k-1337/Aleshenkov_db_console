package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {
    private String HOST;
    private String PORT;
    private String DB_NAME;
    private String LOGIN; // пароль; устанавливается при вводе; = "user19"; Если OpenServer, то здесь mysql напишите
    private String PASS;// пароль; устанавливается при вводе; = "70144"; Если OpenServer, то здесь mysql напишите

    //Нужно для тестирования, поскольку к серверам MySQL НРТК невозможно подключиться извне.
    //Если нет подключения к НРТК, следует использовать localhost и порт на локальном управлении БД.

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
        //ссылка, состоящая из хоста, порта и имени пользователя.
        String connStr = "jdbc:postgresql://"+HOST+":"+PORT+"/"+DB_NAME;
        Class.forName("org.postgresql.Driver");

        //Соединение... (Если логин и пароль правильные, должно сработать)
        DbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return DbConn;
    }

    public void insertTask(int ID, String ISBN, String title, String author, String publisher, int publishYear, String genre, String language, int pagesAmount) throws SQLException, ClassNotFoundException {
        //ISBN - переменная типа long а не int потому, что длина ISBN составляет 13 цифр,
        // в то время, как int поддерживает числа до 2,147,483,647 (без запятых вышла бы каша), что намного меньше
        // чем, например, 9,785,845,916,549 (в формате ISBN - 978-5-8459-1654-9).

        // вернёмся к нашей программе; эта строка - заготовка к команде, с помощью которой мы вставим нужные данные.
        String sql = "INSERT INTO book (idBook, ISBN, title, author, publisher, publishYear, genre, language, pagesAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Тут темно и страшно; команда редактируется, чтобы вместо знаков вопроса были нормальные значения
        PreparedStatement prSt = getDbConn().prepareStatement(sql);
        prSt.setInt(1, ID);
        prSt.setString(2, ISBN);
        prSt.setString(3, title);
        prSt.setString(4, author);
        prSt.setString(5, publisher);
        prSt.setInt(6, publishYear);
        prSt.setString(7, genre);
        prSt.setString(8, language);
        prSt.setInt(9, pagesAmount);

        //Команда запускается; момент истины, если всё пройдёт без проблем - труд проделан не зря и можно выдохнуть.
        //Если не сработает, где-то возможно допущена ошибка.
        prSt.executeUpdate();

    }
}
