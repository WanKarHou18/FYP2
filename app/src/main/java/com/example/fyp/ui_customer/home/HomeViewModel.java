package com.example.fyp.ui_customer.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> printer_json = new MutableLiveData<>();
    public LiveData<String> getprinter_json() {
        return printer_json;
    }
    public void setprinter_json(String printer_json){
        this.printer_json.setValue(printer_json);
    }
}