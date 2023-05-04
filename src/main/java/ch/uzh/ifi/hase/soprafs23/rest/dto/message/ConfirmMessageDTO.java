package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;

public class ConfirmMessageDTO {
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
