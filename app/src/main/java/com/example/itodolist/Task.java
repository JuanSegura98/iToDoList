package com.example.itodolist;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    final String name;
    final String beginDate;
    final String endDate;
    final String measureUnit;
    final int totalUnits;
    final int currentUnits;
    final int progressBar;

    public Task(String name, String beginDate, String endDate, String measureUnit, int totalUnits, int currentUnits, int progressBar) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.measureUnit = measureUnit;
        this.totalUnits = totalUnits;
        this.currentUnits = currentUnits;
        this.progressBar = progressBar;
    }

    // Añadimos parcelable para poder enviar el objeto entre activities
    protected Task(Parcel in) {
        name = in.readString();
        beginDate = in.readString();
        endDate = in.readString();
        measureUnit = in.readString();
        totalUnits = in.readInt();
        currentUnits = in.readInt();
        progressBar = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(beginDate);
        dest.writeString(endDate);
        dest.writeString(measureUnit);
        dest.writeInt(totalUnits);
        dest.writeInt(currentUnits);
        dest.writeInt(progressBar);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}