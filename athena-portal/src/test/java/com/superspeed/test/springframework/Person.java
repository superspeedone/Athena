package com.superspeed.test.springframework;

public class Person {

    private int idCard;
    private String name;

    public Person() {}

    public Person(int idCard, String name) {
        this.idCard = idCard;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Person person = (Person) o;
        //两个对象是否等值，通过idCard来确定
        return this.idCard == person.idCard;
    }

    @Override
    public int hashCode() {
        int result = idCard;
        result = 31 * result + name.hashCode();
        return result;
    }

}
