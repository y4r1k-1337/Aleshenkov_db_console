package org.example;

/*
* КЛАСС СТОЛБЕЦ
* ПРЕДНАЗНАЧЕНИЕ: не копошиться в бесконечно большом количестве массивов,
* а вместо этого копошиться лишь в одном массиве
* Поля:
*   String name - название столбца
*   String type - тип столбца; прописан строкой, чтобы программа потом сама решила
*                   какой тип ей вводить в таблицу. Если тип введён неправильно,
*                   будет ошибка.
* */

public class Column {
    private String name;
    private String type;
    public Column(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    //Вывод названия столбца и его типа

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    //Не знаю, зачем я поставил эти сеттеры, но пусть будут. Может быть сделаю
    //возможность ввода своих столбцов.

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
