package org.gr40in;

public interface ConnectionListener {
    public void connectionOk(ChatConnection connection);
    public void getMessage(String message);
    public void sendMessage(String message);
}
