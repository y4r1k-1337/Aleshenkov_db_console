package org.example;

import java.util.ArrayList;

/*
* КЛАСС ТАБЛИЦА
* ПРЕДНАЗНАЧЕНИЕ: Обеспечить универсальность программы, чтобы можно было ввести свою таблицу.
* Так же с этим классом будет проще работать.
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
