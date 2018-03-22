package com.superspeed.test.serializable;

import java.io.*;

public class Serializable {

    /**
     * 序列化文件保存内容：
     *  序列号
     *  类信息
     *  被序列化的每个变量的类型和值
     *
     * 静态变量属于类，每个序列化对象都会保存
     * transient 成员变量序列化时不会保存
     * @param args
     */
    public static void main(String[] args) {
        try {
            MyClass myClass1 = new MyClass();
            myClass1.setMyClass(new MyClass());
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("1.out"));
            oos.writeObject(myClass1);
            System.out.println(myClass1);
            System.out.println(myClass1.sex.hashCode());

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("1.out"));
            MyClass myClass2 = (MyClass) objectInputStream.readObject();
            System.out.println(myClass2);
            System.out.println(myClass2.myClass.sex.hashCode());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * aced        Stream Magic
     0005        序列化版本号
     73          标志位:TC_OBJECT,表示接下来是个新的Object
     72          标志位:TC_CLASSDESC,表示接下来是对Class的描述
     0020        类名的长度为32
     636f 6d2e 6265 6175 7479 626f 7373 2e73 com.beautyboss.s
     6c6f 6765 6e2e 5465 7374 4f62 6a65 6374 logen.TestObject
     d3c6 7e1c 4f13 2afe 序列号
     02          flag，可序列化
     00 02       TestObject的字段的个数，为2
     49          TypeCode，I，表示int类型
     0009        字段名长度，占9个字节
     7465 7374 5661 6c75 65      字段名:testValue
     4c          TypeCode:L,表示是个Class或者Interface
     000b        字段名长度，占11个字节
     696e 6e65 724f 626a 6563 74 字段名:innerObject
     74          标志位：TC_STRING，表示后面的数据是个字符串
     0023        类名长度，占35个字节
     4c63 6f6d 2f62 6561 7574 7962 6f73 732f  Lcom/beautyboss/
     736c 6f67 656e 2f49 6e6e 6572 4f62 6a65  slogen/InnerObje
     6374 3b                                  ct;
     78          标志位:TC_ENDBLOCKDATA,对象的数据块描述的结束
     */



    static class MyClass implements java.io.Serializable {

        private static final long serialVersionUID = 1L;

        private static int age = 20;

        private transient static String password = "123";

        private transient String username = "zhangsan";

        private String sex = "Man";

        private MyClass myClass;

        public static int getAge() {
            return age;
        }

        public static void setAge(int age) {
            MyClass.age = age;
        }

        public static String getPassword() {
            return password;
        }

        public static void setPassword(String password) {
            MyClass.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public MyClass getMyClass() {
            return myClass;
        }

        public void setMyClass(MyClass myClass) {
            this.myClass = myClass;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", age='" + age + '\'' +
                    ", sex='" + sex + '\'' +
                    ", myClass='" + myClass.getClass() + "@" + myClass.hashCode() + '\'' +
                    '}';
        }
    }

}
