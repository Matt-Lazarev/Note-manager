package com.lazarev.mvc.registration.email;

public interface EmailSender {
    void send(String to, String email);
}
