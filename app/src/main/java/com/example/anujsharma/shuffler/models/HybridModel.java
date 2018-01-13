package com.example.anujsharma.shuffler.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anuj5 on 10-01-2018.
 */

public class HybridModel implements Parcelable {

    public static final Creator<HybridModel> CREATOR = new Creator<HybridModel>() {
        @Override
        public HybridModel createFromParcel(Parcel in) {
            return new HybridModel(in);
        }

        @Override
        public HybridModel[] newArray(int size) {
            return new HybridModel[size];
        }
    };
    private int type;
    private String artworkUrl, typeUrl, name, songArtist;
    private long id;

    public HybridModel(int type, long id, String artworkUrl, String name, String songArtist) {
        this.type = type;
        this.artworkUrl = artworkUrl;
        this.id = id;
        this.name = name;
        this.songArtist = songArtist;
    }

    protected HybridModel(Parcel in) {
        type = in.readInt();
        artworkUrl = in.readString();
        id = in.readLong();
        name = in.readString();
        songArtist = in.readString();
    }

    public int getType() {
        return type;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSongArtist() {
        return songArtist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(artworkUrl);
        parcel.writeString(typeUrl);
        parcel.writeString(name);
        parcel.writeString(songArtist);
        parcel.writeLong(id);
    }
}
