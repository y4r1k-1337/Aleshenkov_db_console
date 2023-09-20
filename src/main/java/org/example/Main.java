package org.example;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Сканнер для ввода выбора
        Scanner in = new Scanner(System.in);
        System.out.println("Введите 0 для локального запуска от имени пользователя postgres");
        int choice = in.nextInt();
        DB db = new DB();
        if (choice == 0)
        {
            db.DB_init("127.0.0.1","5432","books","postgres","postgres");
            // Внизу строка от 20.09.2023 20:41 МСК
            // db.insertTask(1,"9785845916549","SQL. Полное руководство","Грофф Джеймс Р., Оппель Эндрю Дж., Вайнберг Пол Н.","Диалектика",2020,"Учебная литература","Русский",960);
        }
        else
        {
            String host, port, db_name, login, password;
            System.out.printf("host: ");
            host = in.next();
            System.out.println();
            System.out.printf("порт: ");
            port = in.next();
            System.out.println();
            System.out.printf("название БД: ");
            db_name = in.next();
            System.out.println();
            System.out.printf("логин: ");
            login = in.next();
            System.out.println();
            System.out.printf("пароль: ");
            password = in.next();
            System.out.println();
            db.DB_init(host,port,db_name,login,password);
        }

        //массив
        ArrayList<Column> cols = new ArrayList<>();
        System.out.println("Введите название таблицы: ");
        //название таблицы
        String tableName = in.next();

        //получение столбцов
        db.getColumns(tableName, cols);

        Table table = new Table(tableName, cols);

        for (Column col: table.cols)
        {
            System.out.println(col.getType());
        }

        //ввод в таблицу
        db.insertTask(table,in);

        //вывод таблицы
        System.out.print("Сортировать по: ");
        db.outputEverythingTask(table,in.next());
    }
}