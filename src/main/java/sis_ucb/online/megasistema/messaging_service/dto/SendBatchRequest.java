package sis_ucb.online.megasistema.messaging_service.dto;

import java.util.List;

public class SendBatchRequest {
    private List<String> id;
    private String text;

    public List<String> getId() { return id; }
    public void setId(List<String> id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}