package com.fireblend.uitest.bd;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contacts")
public class Contact {

    @DatabaseField(generatedId = true, columnName = "contact_id", canBeNull = false)
    public int contactId;

    @DatabaseField(columnName = "name", canBeNull = false)
    public String name;

    @DatabaseField(columnName = "age", canBeNull = false)
    public int age;

    @DatabaseField(columnName = "email", canBeNull = false)
    public String email;

    @DatabaseField(columnName = "phone", canBeNull = false)
    public String phone;

    Contact() {
        // needed by ormlite
    }

    public Contact(String name, int age, String email, String phone){
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}


