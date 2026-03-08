package com.example.makzholvtipo_35;
public class Student {
    private int id;
    private String name;
    private String group;
    private int age;

    public Student(int id, String name, String group, int age) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getAge() {
        return age;
    }
}