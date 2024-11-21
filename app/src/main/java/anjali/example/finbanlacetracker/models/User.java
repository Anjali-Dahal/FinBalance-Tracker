package anjali.example.finbanlacetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject implements Parcelable {
    private String name;
    private String email;
    private String occupation;
    private String ph_number;
    private String password;

    @PrimaryKey
    private long id;

    public User() {}

    public User(String name, String email, String occupation, String ph_number, String password, long id){
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.ph_number = ph_number;
        this.password = password;
        this.id = id;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        occupation = in.readString();
        ph_number = in.readString();
        password = in.readString();
        id = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getEmail());
        parcel.writeString(getOccupation());
        parcel.writeString(getPh_number());
        parcel.writeString(getPassword());
        parcel.writeLong(getId());
    }
}
