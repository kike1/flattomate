package myapp.flattomate.Model;

/**
 * Created by kike on 24/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pivot")
    @Expose
    private Pivot pivot;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
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
     * The pivot
     */
    public Pivot getPivot() {
        return pivot;
    }

    /**
     *
     * @param pivot
     * The pivot
     */
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

}
