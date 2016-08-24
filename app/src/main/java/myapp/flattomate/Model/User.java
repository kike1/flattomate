package myapp.flattomate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.List;

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

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("birthdate")
    @Expose
    private String birthdate;

//    @SerializedName("languages")
//    @Expose
    private List<Language> languages;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("activity")
    @Expose
    private String activity;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("smoke")
    @Expose
    private Integer smoke;

    @SerializedName("sociable")
    @Expose
    private Integer sociable;

    @SerializedName("tidy")
    @Expose
    private Integer tidy;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("remember_token")
    @Expose
    private String rememberToken;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public User(){}

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
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The birthday
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     *
     * @param birthdate
     * The birthday
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     *
     * @return
     * The languages
     */
    public List<Language> getLanguages() {


        return languages;
    }

    /**
     *
     * @param languagesnew
     * The birthday
     */
    public void setLanguages(List<Language> languagesnew) {
       languages = languagesnew;
    }

    /**
     *
     * @return
     * The age of user
     */
    public int getAges() {
        LocalDate now = new LocalDate();
        Years ages = Years.yearsBetween(new LocalDate(birthdate), now);
        return ages.getYears();
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
     * The sex
     */
    public String getSex() {
        return sex;
    }

    /**
     *
     * @param sex
     * The sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     *
     * @return
     * The smoke
     */
    public Integer getSmoke() {
        return smoke;
    }

    /**
     *
     * @param smoke
     * The smoke
     */
    public void setSmoke(Integer smoke) {
        this.smoke = smoke;
    }

    /**
     *
     * @return
     * The sociable
     */
    public Integer getSociable() {
        return sociable;
    }

    /**
     *
     * @param sociable
     * The sociable
     */
    public void setSociable(Integer sociable) {
        this.sociable = sociable;
    }

    /**
     *
     * @return
     * The tidy
     */
    public Integer getTidy() {
        return tidy;
    }

    /**
     *
     * @param tidy
     * The tidy
     */
    public void setTidy(Integer tidy) {
        this.tidy = tidy;
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
     * The rememberToken
     */
    public String getRememberToken() {
        return rememberToken;
    }

    /**
     *
     * @param rememberToken
     * The remember_token
     */
    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}