package com.flattomate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_main")
    @Expose
    private Integer isMain;
    @SerializedName("id_announcement")
    @Expose
    private Integer idAnnouncement;


    public Image(Integer id, String name, Integer isMain, Integer idad){
        this.id = id;
        this.name = name;
        this.isMain = isMain;
        idAnnouncement = idad;
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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The isMain
     */
    public Integer getIsMain() {
        return isMain;
    }

    /**
     *
     * @param isMain
     * The is_main
     */
    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
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
     * The id_announcement
     */
    public void setIdAnnouncement(Integer idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

}