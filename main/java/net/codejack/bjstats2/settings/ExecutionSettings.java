package net.codejack.bjstats2.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Rome07 on 29-06-17.
 */

public class ExecutionSettings implements Parcelable, Serializable {

    private String dealer = "";
    private String player1 = "";
    private String player2 = "";
    private int loops = 1000;

    public static final Creator<ExecutionSettings> CREATOR = new Creator<ExecutionSettings>() {
        @Override
        public ExecutionSettings createFromParcel(Parcel source) {
            return new ExecutionSettings(source);
        }

        @Override
        public ExecutionSettings[] newArray(int size) {
            return new ExecutionSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(dealer);
        parcel.writeString(player1);
        parcel.writeString(player2);
        parcel.writeInt(loops);
    }

    public ExecutionSettings() {

    }

    public ExecutionSettings(Parcel parcel) {
        this.dealer = parcel.readString();
        this.player1 = parcel.readString();
        this.player2 = parcel.readString();
        this.loops = parcel.readInt();
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getLoops() {
        return loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }
}