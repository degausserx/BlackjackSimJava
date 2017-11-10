package net.codejack.bjstats2.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Rome07 on 29-06-17.
 */

public class HouseStrategy implements Parcelable, Serializable {

    private boolean draw17 = false;
    private boolean holecard = false;
    private boolean doubleonsoft = true;
    private boolean splitacesone = true;
    private boolean splitacesnobjs = true;
    private boolean resplitaces = false;
    private boolean surrender = false;
    private boolean surrendervsace = false;
    private boolean surrenderearly = false;
    private int decks = 1;
    private int maxsplits = 1;

    public boolean getDraw17() {
        return draw17;
    }

    public void setDraw17(boolean draw17) {
        this.draw17 = draw17;
    }

    public boolean getHolecard() {
        return holecard;
    }

    public void setHolecard(boolean holecard) {
        this.holecard = holecard;
    }

    public boolean getDoubleonsoft() {
        return doubleonsoft;
    }

    public void setDoubleonsoft(boolean doubleonsoft) {
        this.doubleonsoft = doubleonsoft;
    }

    public boolean getSplitacesone() {
        return splitacesone;
    }

    public void setSplitacesone(boolean splitacesone) {
        this.splitacesone = splitacesone;
    }

    public boolean getSplitacesnobjs() {
        return splitacesnobjs;
    }

    public void setSplitacesnobjs(boolean splitacesnobjs) {
        this.splitacesnobjs = splitacesnobjs;
    }

    public boolean getResplitaces() {
        return resplitaces;
    }

    public void setResplitaces(boolean resplitaces) {
        this.resplitaces = resplitaces;
    }

    public boolean getSurrender() {
        return surrender;
    }

    public void setSurrender(boolean surrender) {
        this.surrender = surrender;
    }

    public boolean getSurrendervsace() {
        return surrendervsace;
    }

    public void setSurrendervsace(boolean surrendervsace) {
        this.surrendervsace = surrendervsace;
    }

    public boolean getSurrenderearly() {
        return surrenderearly;
    }

    public void setSurrenderearly(boolean surrenderearly) {
        this.surrenderearly = surrenderearly;
    }

    public int getDecks() {
        return decks;
    }

    public void setDecks(int decks) {
        this.decks = decks;
    }

    public int getMaxsplits() {
        return maxsplits;
    }

    public void setMaxsplits(int maxsplits) {
        this.maxsplits = maxsplits;
    }

    public static final Creator<HouseStrategy> CREATOR = new Creator<HouseStrategy>() {
        @Override
        public HouseStrategy createFromParcel(Parcel source) {
            return new HouseStrategy(source);
        }

        @Override
        public HouseStrategy[] newArray(int size) {
            return new HouseStrategy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(draw17 ? 1 : 0);
        parcel.writeInt(holecard ? 1 : 0);
        parcel.writeInt(doubleonsoft ? 1 : 0);
        parcel.writeInt(splitacesone ? 1 : 0);
        parcel.writeInt(splitacesnobjs ? 1 : 0);
        parcel.writeInt(resplitaces ? 1 : 0);
        parcel.writeInt(surrender ? 1 : 0);
        parcel.writeInt(surrendervsace ? 1 : 0);
        parcel.writeInt(surrenderearly ? 1 : 0);
        parcel.writeInt(decks);
        parcel.writeInt(maxsplits);
    }

    public HouseStrategy() {

    }

    public HouseStrategy(Parcel parcel) {
        this.draw17 = parcel.readInt() != 0;
        this.holecard = parcel.readInt() != 0;
        this.doubleonsoft = parcel.readInt() != 0;
        this.splitacesone = parcel.readInt() != 0;
        this.splitacesnobjs = parcel.readInt() != 0;
        this.resplitaces = parcel.readInt() != 0;
        this.surrender = parcel.readInt() != 0;
        this.surrendervsace = parcel.readInt() != 0;
        this.surrenderearly = parcel.readInt() != 0;
        this.decks = parcel.readInt();
        this.maxsplits = parcel.readInt();
    }
}