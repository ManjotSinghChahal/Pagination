package app.com.pagination.recyclerviewpagination;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ItemModel implements Parcelable,Cloneable {

    @SerializedName("url")
    private String imageUrl;

    protected ItemModel(Parcel in) {
        imageUrl = in.readString();
    }
    public ItemModel() {

    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setSetImageUrl(String url) {
        imageUrl = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }
}
