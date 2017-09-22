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
    @SerializedName("id_user_receive")
    @Expose
    private Integer idUserReceiver;
    @SerializedName("id_announcement")
    @Expose
    private Integer idAnnouncement;
    @SerializedName("rating")
    @Expose
    private float rating;

    public Review(String description, Integer idUserWrote,
                  Integer idUserReceiver, Integer idAnnouncement, float rating){

        this.description = description;
        this.idUserWrote = idUserWrote;
        this.idUserReceiver = idUserReceiver;
        this.idAnnouncement = idAnnouncement;
        this.rating = rating;
    }
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

    public Integer getIdUserReceiver() {
        return idUserReceiver;
    }

    public void setIdUserReceiver(Integer idUserReceiver) {
        this.idUserReceiver = idUserReceiver;
    }

    /**
     *
     * @return
     * The idAnnouncement
     */
    public Integer getIdAnnouncement() {
        return idAnnouncement;
    }

    /**
     *
     * @param idAnnouncement
     * The idAnnouncement
     */
    public void setIdAnnouncement(Integer idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    /**
     *
     * @return
     * The rating
     */
    public float getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setId(float rating) {
        this.rating = rating;
    }

}