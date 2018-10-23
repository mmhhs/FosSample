package com.little.popup.listener;

public interface IOnStringDialogListener {
    void onConfirm(String value);
    void onCancel();
    void onOther();
}