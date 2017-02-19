package com.flattomate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("id_user_wrote")
    @Expose
    private Integer idUserWrote;
    @SerializedName("id_user_receive")
    @Expose
    private Integer idUserReceive;
    @SerializedName("id_announcement")
    @Expose
    private Integer idAnnouncement;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("viewed")
    @Expose
    private Boolean viewed;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Chat(Integer idUserR, Integer idUserW, Integer idAnnouncement, String message){
        idUserReceive = idUserR;
        idUserWrote = idUserW;
        this.idAnnouncement = idAnnouncement;
        this.message = message;
    }

    public Integer getIdUserWrote() {
        return idUserWrote;
    }

    public void setIdUserWrote(Integer idUserWrote) {
        this.idUserWrote = idUserWrote;
    }

    public Integer getIdUserReceive() {
        return idUserReceive;
    }

    public void setIdUserReceive(Integer idUserReceive) {
        this.idUserReceive = idUserReceive;
    }

    public Integer getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(Integer idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}