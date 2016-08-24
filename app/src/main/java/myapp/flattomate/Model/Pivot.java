package myapp.flattomate.Model;

/**
 * Created by kike on 24/8/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot {

    @SerializedName("id_user")
    @Expose
    private int idUser;
    @SerializedName("id_language")
    @Expose
    private int idLanguage;

    /**
     *
     * @return
     * The idUser
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     *
     * @param idUser
     * The id_user
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     *
     * @return
     * The idLanguage
     */
    public int getIdLanguage() {
        return idLanguage;
    }

    /**
     *
     * @param idLanguage
     * The id_language
     */
    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

}