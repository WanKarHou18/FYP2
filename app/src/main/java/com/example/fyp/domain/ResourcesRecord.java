package com.example.fyp.domain;

public class ResourcesRecord {

    private String resources_id;
    private String file_name;
    private String file_type;
    private String file_description;
    private String sub_order_id;


    public ResourcesRecord(){

    }

    public ResourcesRecord(String resources_id,String file_name,String file_type,String file_description,String sub_order_id){
        this.resources_id=resources_id;
        this.file_name=file_name;
        this.file_type=file_type;
        this.file_description=file_description;
        this.sub_order_id=sub_order_id;
    }

    public String getfile_name() {
        return file_name;
    }

    public void setfile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getfile_description() {
        return file_description;
    }

    public void setfile_description(String file_description) {
        this.file_description = file_description;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getresources_id() {
        return resources_id;
    }

    public void setresources_id(String resources_id) {
        this.resources_id = resources_id;
    }

    public String getSubOrderId() {
        return sub_order_id;
    }

    public void setSubOrderId(String subOrderId) {
        this.sub_order_id = sub_order_id;
    }
}
