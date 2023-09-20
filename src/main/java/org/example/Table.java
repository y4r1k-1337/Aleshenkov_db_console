package org.example;

import java.util.ArrayList;

/*
* КЛАСС ТАБЛИЦЫ
* ПРЕДНАЗНАЧЕНИЕ: Обеспечить универсальность программы, чтобы можно было ввести свою таблицу.
* */

public class Table {
    String name;
    ArrayList<Column> cols;
    public Table(String name, ArrayList<Column> cols)
    {
        this.name = name;
        this.cols = cols;
    }
}
