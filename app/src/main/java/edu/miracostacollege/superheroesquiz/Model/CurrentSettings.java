package edu.miracostacollege.superheroesquiz.Model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * This just holds what type of quiz we are taking right now
 * */

public class CurrentSettings implements Parcelable {

    public enum QuizType {
        NAMES,
        ONE_THING,
        SUPERPOWERS
    }

    private QuizType quizType;

    public CurrentSettings(QuizType quizType) {
        this.quizType = quizType;
    }

    public QuizType getQuizType() {
        return quizType;
    }

    public void setQuizType(QuizType quizType) {
        this.quizType = quizType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quizType == null ? -1 : this.quizType.ordinal());
    }

    private CurrentSettings(Parcel in) {
        int tmpQuizType = in.readInt();
        this.quizType = tmpQuizType == -1 ? null : QuizType.values()[tmpQuizType];
    }

    public static final Parcelable.Creator<CurrentSettings> CREATOR = new Parcelable.Creator<CurrentSettings>() {
        @Override
        public CurrentSettings createFromParcel(Parcel source) {
            return new CurrentSettings(source);
        }

        @Override
        public CurrentSettings[] newArray(int size) {
            return new CurrentSettings[size];
        }
    };
}
