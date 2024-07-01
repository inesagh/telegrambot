package com.bot.telegram.dto;

import java.util.List;

public class ChatGPTRequest {
    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatGPTRequest(String model, String message) {
        this.model = model;
        this.messages = List.of(new Message("user", message));
        this.n = 1;
        this.temperature = 1.5;
    }

    public ChatGPTRequest(String model, String message, int n, double temperature) {
        this.model = model;
        this.messages = List.of(new Message("user", message));
        this.n = n;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override public String toString() {
        return "ChatGPTRequest{" + "model='" + model + '\'' + ", messages=" + messages + ", n=" + n + ", temp=" + temperature
                + '}';
    }
}
