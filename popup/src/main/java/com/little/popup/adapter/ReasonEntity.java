package com.little.popup.adapter;

import java.io.Serializable;

public class ReasonEntity implements Serializable {

    public String reason;
    public boolean isSelected = false;

    public ReasonEntity(String reason) {
        this.reason = reason;
    }
}
