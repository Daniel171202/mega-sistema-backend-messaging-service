package sis_ucb.online.megasistema.messaging_service.dto;

public class SendMessageRequest {
    private String id;
    private String text;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}