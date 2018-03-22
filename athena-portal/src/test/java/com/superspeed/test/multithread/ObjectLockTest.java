package com.superspeed.test.multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ObjectLockTest {

    public static void main(String[] args) {
        final InsureInfo insureInfo1 = new InsureInfo();
        insureInfo1.setIcOrderId("SHJH20180321161004");

        InsureInfo insureInfo2 = new InsureInfo();
        insureInfo2.setIcOrderId("SHJH20180321161004");

        final Map<String, Object> params1 = new HashMap<String, Object>(1);
        params1.put("InsureInfo", insureInfo1);

        final Map<String, Object> params2 = new HashMap<String, Object>(1);
        params2.put("InsureInfo", insureInfo2);

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();

                InsureInfo insureInfo = (InsureInfo) params1.get("InsureInfo");

                synchronized (insureInfo.getIcOrderId()) {
                    System.out.printf("%d Thread-1 [%d] is running. IcOrderId:[%s] \n",
                            System.currentTimeMillis(), Thread.currentThread().getId(),
                            insureInfo.getIcOrderId());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();

                InsureInfo insureInfo = (InsureInfo) params2.get("InsureInfo");

                synchronized (insureInfo.getIcOrderId()) {
                    System.out.printf("%d Thread-2 [%d] is running. IcOrderId:[%s] \n",
                            System.currentTimeMillis(), Thread.currentThread().getId(),
                            insureInfo.getIcOrderId());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class InsureInfo {

        private String icOrderId;

        public String getIcOrderId() {
            return icOrderId;
        }

        public void setIcOrderId(String icOrderId) {
            this.icOrderId = icOrderId;
        }
    }

}
