package myapp.flattomate.Model;

/**
 * Created by kike on 15/5/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import javax.annotation.Generated;

//@Generated("org.jsonschema2pojo")
public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("member_since")
    @Expose
    private String memberSince;

    @SerializedName("password")
    private String password;


    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;

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
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     * The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     *
     * @param activity
     * The activity
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     *
     * @return
     * The bio
     */
    public String getBio() {
        return bio;
    }

    /**
     *
     * @param bio
     * The bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     *
     * @return
     * The memberSince
     */
    public String getMemberSince() {
        return memberSince;
    }

    /**
     *
     * @param memberSince
     * The member_since
     */
    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }


}