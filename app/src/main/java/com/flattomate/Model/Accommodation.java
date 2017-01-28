package com.flattomate.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accommodation implements Parcelable{

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
    @SerializedName("id_user")
    @Expose
    private Integer idUser;

    public Accommodation() {}

    protected Accommodation(Parcel in) {
        location = in.readString();
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };

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

    /**
     *
     * @return
     * The idUser
     */
    public Integer getIdUser() {
        return idUser;
    }

    /**
     *
     * @param idUser
     * The idUser
     */
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if(id != null)
            dest.writeInt(id);
        if(nPeople != null)
            dest.writeInt(nPeople);
        if(nBeds != null)
            dest.writeInt(nBeds);
        if(nBathrooms != null)
            dest.writeInt(nBathrooms);
        if(nRooms != null)
            dest.writeInt(nRooms);
        if(location != null)
            dest.writeString(location);
        if(idAnnouncement != null)
            dest.writeInt(idAnnouncement);
        if(idUser != null)
            dest.writeInt(idUser);
    }

}