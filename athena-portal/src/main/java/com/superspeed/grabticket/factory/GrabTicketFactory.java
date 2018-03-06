package com.superspeed.grabticket.factory;

import com.superspeed.grabticket.GrabTicket;

public class GrabTicketFactory {

    private static GrabTicket instance;

    public static GrabTicket getInstance() {
        synchronized (GrabTicketFactory.class) {
            if (instance == null) {
                synchronized(GrabTicketFactory.class) {
                    if (instance == null) {
                        instance = new GrabTicket();
                    }
                }
            }
            return instance;
        }
    }


}
