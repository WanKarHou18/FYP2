package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DocumentPrintingSetting implements Serializable {
    @SerializedName("doc_printing_id")
    @Expose
    private String doc_printing_id;

    @SerializedName("doc_print_pref_json")
    @Expose
    private DocumentPrintingSettingPreferences doc_print_pref_json;

    public DocumentPrintingSetting(String doc_printing_id,DocumentPrintingSettingPreferences doc_print_pref_json){
        this.doc_printing_id = doc_printing_id;
        this.doc_print_pref_json = doc_print_pref_json;
    }
    public String getdoc_printing_id() {
        return doc_printing_id;
    }
    public void setdoc_printing_id(String doc_printing_id){
        this.doc_printing_id = doc_printing_id;
    }

    public DocumentPrintingSettingPreferences getdocumentPrintingSettingPreferences() {
        return doc_print_pref_json;
    }
    public void setdocumentPrintingSettingPreferences(DocumentPrintingSettingPreferences doc_print_pref_json){
        this.doc_print_pref_json = doc_print_pref_json;
    }

    //JSON to OBJ
    public static DocumentPrintingSetting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        DocumentPrintingSetting DocumentPrintingSetting= gson.fromJson(json, DocumentPrintingSetting.class);
        return DocumentPrintingSetting;
    }

    //OBJ to JSON
    public static String OBJtoJSON(DocumentPrintingSetting DocumentPrintingSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPrintingSetting);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<DocumentPrintingSetting> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DocumentPrintingSetting>>(){}.getType();
        ArrayList<DocumentPrintingSetting> DocumentPrintingSetting = gson.fromJson(json, listType);
        return DocumentPrintingSetting;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<DocumentPrintingSetting> DocumentPrintingSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPrintingSetting);
        return JSON;
    }
}
