package net.codejack.bjstats2.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Degausser on 6/28/2017.
 */

public class PlayerStrategy implements Parcelable, Serializable {

    private HashMap<String, HashMap<Integer, String>> basic;
    private String epoch;

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new PlayerStrategy(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new PlayerStrategy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(epoch);
        parcel.writeInt(basic.size());

        for (HashMap.Entry<String, HashMap<Integer, String>> entry : basic.entrySet()) {
            final HashMap<Integer, String> list = entry.getValue();
            parcel.writeString(entry.getKey());
            parcel.writeInt(list.size());

            for (HashMap.Entry<Integer, String> item: list.entrySet()) {
                parcel.writeInt(item.getKey());
                parcel.writeString(item.getValue());
            }
        }
    }

    private PlayerStrategy(Parcel parcel) {
        epoch = parcel.readString();
        int size = parcel.readInt();
        basic = new HashMap<>(size);

        for (int i = 0; i < size; i++) {
            final String player = parcel.readString();
            final int hash_size = parcel.readInt();
            final HashMap<Integer, String> list = new HashMap<>(hash_size);

            for (int j = 0; j < hash_size; j++) {
                final int dealer = parcel.readInt();
                final String move = parcel.readString();
                list.put(dealer, move);
            }

            this.basic.put(player, list);
        }
    }

    public PlayerStrategy() {
        epoch = "" + System.currentTimeMillis();
        basic = new HashMap<>();
        loadDefaults();
    }

    public String getBaseStrat(String player, int dealer) {
        return basic.get(player).get(dealer);
    }

    public void putBaseStrat(String player, int dealer, String value) {
        HashMap<Integer, String> map_2 = basic.get(player);
        map_2.put(dealer, value);
        basic.put(player, map_2);
    }

    public String toString() {
        return epoch;
    }

    public HashMap<String, HashMap<Integer, String>> getMap() {
        return basic;
    }

    private void loadDefaults() {
        HashMap<Integer, String> map_2 = new HashMap<>();
        HashMap<Integer, String> map_5 = new HashMap<>();
        HashMap<Integer, String> map_9 = new HashMap<>();
        HashMap<Integer, String> map_10 = new HashMap<>();
        HashMap<Integer, String> map_12 = new HashMap<>();
        HashMap<Integer, String> map_13 = new HashMap<>();
        HashMap<Integer, String> map_14 = new HashMap<>();
        HashMap<Integer, String> map_15 = new HashMap<>();
        HashMap<Integer, String> map_16 = new HashMap<>();
        HashMap<Integer, String> map_17 = new HashMap<>();
        HashMap<Integer, String> map_18 = new HashMap<>();

        HashMap<Integer, String> map_A2 = new HashMap<>();
        HashMap<Integer, String> map_A4 = new HashMap<>();
        HashMap<Integer, String> map_A6 = new HashMap<>();
        HashMap<Integer, String> map_A7 = new HashMap<>();

        HashMap<Integer, String> map_22 = new HashMap<>();
        HashMap<Integer, String> map_33 = new HashMap<>();
        HashMap<Integer, String> map_44 = new HashMap<>();
        HashMap<Integer, String> map_55 = new HashMap<>();
        HashMap<Integer, String> map_66 = new HashMap<>();
        HashMap<Integer, String> map_77 = new HashMap<>();
        HashMap<Integer, String> map_88 = new HashMap<>();
        HashMap<Integer, String> map_99 = new HashMap<>();
        HashMap<Integer, String> map_TT = new HashMap<>();
        HashMap<Integer, String> map_AA = new HashMap<>();

        map_2.put(2, "H");
        map_2.put(3, "H");
        map_2.put(4, "H");
        map_2.put(5, "H");
        map_2.put(6, "H");
        map_2.put(7, "H");
        map_2.put(8, "H");
        map_2.put(9, "H");
        map_2.put(10, "H");
        map_2.put(11, "H");
        basic.put("3", map_2);
        basic.put("4", map_2);
        basic.put("8", map_2);

        map_5.put(2, "H");
        map_5.put(3, "H");
        map_5.put(4, "H");
        map_5.put(5, "H");
        map_5.put(6, "H");
        map_5.put(7, "H");
        map_5.put(8, "H");
        map_5.put(9, "H");
        map_5.put(10, "H");
        map_5.put(11, "A");
        basic.put("5", map_5);
        basic.put("6", map_5);
        basic.put("7", map_5);

        map_9.put(2, "H");
        map_9.put(3, "D");
        map_9.put(4, "D");
        map_9.put(5, "D");
        map_9.put(6, "D");
        map_9.put(7, "H");
        map_9.put(8, "H");
        map_9.put(9, "H");
        map_9.put(10, "H");
        map_9.put(11, "H");
        basic.put("9", map_9);

        map_10.put(2, "D");
        map_10.put(3, "D");
        map_10.put(4, "D");
        map_10.put(5, "D");
        map_10.put(6, "D");
        map_10.put(7, "D");
        map_10.put(8, "D");
        map_10.put(9, "D");
        map_10.put(10, "H");
        map_10.put(11, "H");
        basic.put("10", map_10);
        basic.put("11", map_10);

        map_12.put(2, "H");
        map_12.put(3, "H");
        map_12.put(4, "X");
        map_12.put(5, "X");
        map_12.put(6, "X");
        map_12.put(7, "H");
        map_12.put(8, "H");
        map_12.put(9, "H");
        map_12.put(10, "H");
        map_12.put(11, "A");
        basic.put("12", map_12);

        map_13.put(2, "H");
        map_13.put(3, "X");
        map_13.put(4, "X");
        map_13.put(5, "X");
        map_13.put(6, "X");
        map_13.put(7, "H");
        map_13.put(8, "H");
        map_13.put(9, "H");
        map_13.put(10, "H");
        map_13.put(11, "A");
        basic.put("13", map_13);

        map_14.put(2, "X");
        map_14.put(3, "X");
        map_14.put(4, "X");
        map_14.put(5, "X");
        map_14.put(6, "X");
        map_14.put(7, "H");
        map_14.put(8, "H");
        map_14.put(9, "H");
        map_14.put(10, "A");
        map_14.put(11, "A");
        basic.put("14", map_14);

        map_15.put(2, "X");
        map_15.put(3, "X");
        map_15.put(4, "X");
        map_15.put(5, "X");
        map_15.put(6, "X");
        map_15.put(7, "H");
        map_15.put(8, "H");
        map_15.put(9, "H");
        map_15.put(10, "A");
        map_15.put(11, "A");
        basic.put("15", map_15);

        map_16.put(2, "X");
        map_16.put(3, "X");
        map_16.put(4, "X");
        map_16.put(5, "X");
        map_16.put(6, "X");
        map_16.put(7, "H");
        map_16.put(8, "H");
        map_16.put(9, "A");
        map_16.put(10, "A");
        map_16.put(11, "A");
        basic.put("16", map_16);

        map_17.put(2, "X");
        map_17.put(3, "X");
        map_17.put(4, "X");
        map_17.put(5, "X");
        map_17.put(6, "X");
        map_17.put(7, "X");
        map_17.put(8, "X");
        map_17.put(9, "X");
        map_17.put(10, "X");
        map_17.put(11, "A");
        basic.put("17", map_17);

        map_18.put(2, "X");
        map_18.put(3, "X");
        map_18.put(4, "X");
        map_18.put(5, "X");
        map_18.put(6, "X");
        map_18.put(7, "X");
        map_18.put(8, "X");
        map_18.put(9, "X");
        map_18.put(10, "X");
        map_18.put(11, "X");
        basic.put("18", map_18);
        basic.put("19", map_18);
        basic.put("20", map_18);





        map_A2.put(2, "H");
        map_A2.put(3, "H");
        map_A2.put(4, "H");
        map_A2.put(5, "D");
        map_A2.put(6, "D");
        map_A2.put(7, "H");
        map_A2.put(8, "H");
        map_A2.put(9, "H");
        map_A2.put(10, "H");
        map_A2.put(11, "H");
        basic.put("A2", map_A2);
        basic.put("A3", map_A2);

        map_A4.put(2, "H");
        map_A4.put(3, "H");
        map_A4.put(4, "D");
        map_A4.put(5, "D");
        map_A4.put(6, "D");
        map_A4.put(7, "H");
        map_A4.put(8, "H");
        map_A4.put(9, "H");
        map_A4.put(10, "H");
        map_A4.put(11, "H");
        basic.put("A4", map_A4);
        basic.put("A5", map_A4);

        map_A6.put(2, "H");
        map_A6.put(3, "D");
        map_A6.put(4, "D");
        map_A6.put(5, "D");
        map_A6.put(6, "D");
        map_A6.put(7, "H");
        map_A6.put(8, "H");
        map_A6.put(9, "H");
        map_A6.put(10, "H");
        map_A6.put(11, "H");
        basic.put("A6", map_A6);

        map_A7.put(2, "X");
        map_A7.put(3, "E");
        map_A7.put(4, "E");
        map_A7.put(5, "E");
        map_A7.put(6, "E");
        map_A7.put(7, "X");
        map_A7.put(8, "X");
        map_A7.put(9, "H");
        map_A7.put(10, "H");
        map_A7.put(11, "H");
        basic.put("A7", map_A7);


        basic.put("A8", map_18);
        basic.put("A9", map_18);

        map_AA.put(2, "S");
        map_AA.put(3, "S");
        map_AA.put(4, "S");
        map_AA.put(5, "S");
        map_AA.put(6, "S");
        map_AA.put(7, "S");
        map_AA.put(8, "S");
        map_AA.put(9, "S");
        map_AA.put(10, "S");
        map_AA.put(11, "H");
        basic.put("AA", map_AA);

        map_22.put(2, "S");
        map_22.put(3, "S");
        map_22.put(4, "S");
        map_22.put(5, "S");
        map_22.put(6, "S");
        map_22.put(7, "S");
        map_22.put(8, "H");
        map_22.put(9, "H");
        map_22.put(10, "H");
        map_22.put(11, "H");
        basic.put("22", map_22);

        map_33.put(2, "S");
        map_33.put(3, "S");
        map_33.put(4, "S");
        map_33.put(5, "S");
        map_33.put(6, "S");
        map_33.put(7, "S");
        map_33.put(8, "H");
        map_33.put(9, "H");
        map_33.put(10, "H");
        map_33.put(11, "A");
        basic.put("33", map_33);

        map_44.put(2, "H");
        map_44.put(3, "H");
        map_44.put(4, "H");
        map_44.put(5, "S");
        map_44.put(6, "S");
        map_44.put(7, "H");
        map_44.put(8, "H");
        map_44.put(9, "H");
        map_44.put(10, "H");
        map_44.put(11, "H");
        basic.put("44", map_44);

        map_55.put(2, "D");
        map_55.put(3, "D");
        map_55.put(4, "D");
        map_55.put(5, "D");
        map_55.put(6, "D");
        map_55.put(7, "D");
        map_55.put(8, "D");
        map_55.put(9, "D");
        map_55.put(10, "H");
        map_55.put(11, "H");
        basic.put("55", map_55);

        map_66.put(2, "S");
        map_66.put(3, "S");
        map_66.put(4, "S");
        map_66.put(5, "S");
        map_66.put(6, "S");
        map_66.put(7, "H");
        map_66.put(8, "H");
        map_66.put(9, "H");
        map_66.put(10, "H");
        map_66.put(11, "A");
        basic.put("66", map_66);

        map_77.put(2, "S");
        map_77.put(3, "S");
        map_77.put(4, "S");
        map_77.put(5, "S");
        map_77.put(6, "S");
        map_77.put(7, "S");
        map_77.put(8, "H");
        map_77.put(9, "H");
        map_77.put(10, "A");
        map_77.put(11, "A");
        basic.put("77", map_77);

        map_88.put(2, "S");
        map_88.put(3, "S");
        map_88.put(4, "S");
        map_88.put(5, "S");
        map_88.put(6, "S");
        map_88.put(7, "S");
        map_88.put(8, "S");
        map_88.put(9, "S");
        map_88.put(10, "A");
        map_88.put(11, "A");
        basic.put("88", map_88);

        map_99.put(2, "S");
        map_99.put(3, "S");
        map_99.put(4, "S");
        map_99.put(5, "S");
        map_99.put(6, "S");
        map_99.put(7, "X");
        map_99.put(8, "S");
        map_99.put(9, "S");
        map_99.put(10, "X");
        map_99.put(11, "X");
        basic.put("99", map_99);

        map_TT.put(2, "X");
        map_TT.put(3, "X");
        map_TT.put(4, "X");
        map_TT.put(5, "X");
        map_TT.put(6, "X");
        map_TT.put(7, "X");
        map_TT.put(8, "X");
        map_TT.put(9, "X");
        map_TT.put(10, "X");
        map_TT.put(11, "X");
        basic.put("TT", map_TT);
    }
}