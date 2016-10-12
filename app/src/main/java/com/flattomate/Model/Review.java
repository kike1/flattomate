package com.flattomate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id_user_wrote")
    @Expose
    private Integer idUserWrote;
    @SerializedName("id_user_received")
    @Expose
    private Integer idUserReceived;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The idUserWrote
     */
    public Integer getIdUserWrote() {
        return idUserWrote;
    }

    /**
     *
     * @param idUserWrote
     * The id_user_wrote
     */
    public void setIdUserWrote(Integer idUserWrote) {
        this.idUserWrote = idUserWrote;
    }

    /**
     *
     * @return
     * The idUserReceived
     */
    public Integer getIdUserReceived() {
        return idUserReceived;
    }

    /**
     *
     * @param idUserReceived
     * The id_user_received
     */
    public void setIdUserReceived(Integer idUserReceived) {
        this.idUserReceived = idUserReceived;
    }

}