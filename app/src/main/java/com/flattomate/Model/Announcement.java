package com.flattomate.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Announcement implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("availability")
    @Expose
    private Date availability;
    @SerializedName("rent_kind")
    @Expose
    private Integer rent_kind;
    @SerializedName("min_stay")
    @Expose
    private Integer minStay;
    @SerializedName("max_stay")
    @Expose
    private Integer maxStay;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("is_visible")
    @Expose
    private Integer isVisible;
    @SerializedName("is_shared_room")
    @Expose
    private Integer isSharedRoom;
    @SerializedName("id_accommodation")
    @Expose
    private Integer idAccommodation;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;

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
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * The availability
     */
    public Date getAvailability() {
        return availability;
    }

    /**
     *
     * @param availability
     * The availability
     */
    public void setAvailability(Date availability) {
        this.availability = availability;
    }

    /**
     *
     * @return
     * The minStay
     */
    public Integer getMinStay() {
        return minStay;
    }

    /**
     *
     * @param minStay
     * The min_stay
     */
    public void setMinStay(Integer minStay) {
        this.minStay = minStay;
    }

    /**
     *
     * @return
     * The maxStay
     */
    public Integer getMaxStay() {
        return maxStay;
    }

    /**
     *
     * @param maxStay
     * The max_stay
     */
    public void setMaxStay(Integer maxStay) {
        this.maxStay = maxStay;
    }

    /**
     *
     * @return
     * The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The isVisible
     */
    public Integer getIsVisible() {
        return isVisible;
    }

    /**
     *
     * @param isVisible
     * The is_visible
     */
    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    /**
     *
     * @return
     * The isSharedRoom
     */
    public Integer getIsSharedRoom() {
        return isSharedRoom;
    }

    /**
     *
     * @param isSharedRoom
     * The is_shared_room
     */
    public void setIsSharedRoom(Integer isSharedRoom) {
        this.isSharedRoom = isSharedRoom;
    }

    /**
     *
     * @return
     * The idAccommodation
     */
    public Integer getIdAccommodation() {
        return idAccommodation;
    }

    /**
     *
     * @param idAccommodation
     * The id_accommodation
     */
    public void setIdAccommodation(Integer idAccommodation) {
        this.idAccommodation = idAccommodation;
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
     * The id_user
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
        if(title != null)
            dest.writeString(title);
        if(description != null)
            dest.writeString(description);
        if(availability != null)
            dest.writeString(availability.toString());
        if(minStay != null)
            dest.writeInt(minStay);
        if(maxStay != null)
            dest.writeInt(maxStay);
        if(price != null)
            dest.writeDouble(price);
        if(isVisible != null)
            dest.writeInt(isVisible);
        if(isSharedRoom != null)
            dest.writeInt(isSharedRoom);
        if(idAccommodation != null)
            dest.writeInt(idAccommodation);
        if(idUser != null)
            dest.writeInt(idUser);

    }

    public Integer getRent_kind() {
        return rent_kind;
    }

    public void setRent_kind(Integer rent_kind) {
        this.rent_kind = rent_kind;
    }
}