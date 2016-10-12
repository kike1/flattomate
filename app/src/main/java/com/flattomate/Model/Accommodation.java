package com.flattomate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accommodation {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("n_people")
    @Expose
    private Integer nPeople;
    @SerializedName("n_beds")
    @Expose
    private Integer nBeds;
    @SerializedName("n_bathrooms")
    @Expose
    private Integer nBathrooms;
    @SerializedName("n_rooms")
    @Expose
    private Integer nRooms;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("id_announcement")
    @Expose
    private Integer idAnnouncement;

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
     * The nPeople
     */
    public Integer getNPeople() {
        return nPeople;
    }

    /**
     *
     * @param nPeople
     * The n_people
     */
    public void setNPeople(Integer nPeople) {
        this.nPeople = nPeople;
    }

    /**
     *
     * @return
     * The nBeds
     */
    public Integer getNBeds() {
        return nBeds;
    }

    /**
     *
     * @param nBeds
     * The n_beds
     */
    public void setNBeds(Integer nBeds) {
        this.nBeds = nBeds;
    }

    /**
     *
     * @return
     * The nBathrooms
     */
    public Integer getNBathrooms() {
        return nBathrooms;
    }

    /**
     *
     * @param nBathrooms
     * The n_bathrooms
     */
    public void setNBathrooms(Integer nBathrooms) {
        this.nBathrooms = nBathrooms;
    }

    /**
     *
     * @return
     * The nRooms
     */
    public Integer getNRooms() {
        return nRooms;
    }

    /**
     *
     * @param nRooms
     * The n_rooms
     */
    public void setNRooms(Integer nRooms) {
        this.nRooms = nRooms;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
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