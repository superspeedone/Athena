package com.superspeed.test.jvm;

public class JavaVMStackSOF {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        /*List<OOMObject> oomObjectList = new ArrayList<>();

        OOMObject oomObject = null;

        while(true) {
            oomObject = new OOMObject();
            oomObjectList.add(oomObject);
        }*/

        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        javaVMStackSOF.stackLeak();
    }

    /**
     * 被代理类
     */
    static class OOMObject {

    }

}
