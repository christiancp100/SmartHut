package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import com.google.gson.annotations.SerializedName;

/** A collection of Semantic UI icons */
@SuppressWarnings("unused")
public enum Icon {
    @SerializedName("home")
    HOME("home"),
    @SerializedName("coffee")
    COFFEE("coffee"),
    @SerializedName("beer")
    BEER("beer"),
    @SerializedName("glass martini")
    GLASS_MARTINI("glass martini"),
    @SerializedName("film")
    FILM("film"),
    @SerializedName("video")
    VIDEO("video"),
    @SerializedName("music")
    MUSIC("music"),
    @SerializedName("headphones")
    HEADPHONES("headphones"),
    @SerializedName("fax")
    FAX("fax"),
    @SerializedName("phone")
    PHONE("phone"),
    @SerializedName("laptop")
    LAPTOP("laptop"),
    @SerializedName("bath")
    BATH("bath"),
    @SerializedName("shower")
    SHOWER("shower"),
    @SerializedName("bed")
    BED("bed"),
    @SerializedName("child")
    CHILD("child"),
    @SerializedName("warehouse")
    WAREHOUSE("warehouse"),
    @SerializedName("car")
    CAR("car"),
    @SerializedName("bicycle")
    BICYCLE("bicycle"),
    @SerializedName("motorcycle")
    MOTORCYCLE("motorcycle"),
    @SerializedName("archive")
    ARCHIVE("archive"),
    @SerializedName("boxes")
    BOXES("boxes"),
    @SerializedName("cubes")
    CUBES("cubes"),
    @SerializedName("chess")
    CHESS("chess"),
    @SerializedName("gamepad")
    GAMEPAD("gamepad"),
    @SerializedName("futbol")
    FUTBOL("futbol"),
    @SerializedName("table tennis")
    TABLE_TENNIS("table tennis"),
    @SerializedName("server")
    SERVER("server"),
    @SerializedName("tv")
    TV("tv"),
    @SerializedName("heart")
    HEART("heart"),
    @SerializedName("camera")
    CAMERA("camera"),
    @SerializedName("trophy")
    TROPHY("trophy"),
    @SerializedName("wrench")
    WRENCH("wrench"),
    @SerializedName("image")
    IMAGE("image"),
    @SerializedName("book")
    BOOK("book"),
    @SerializedName("university")
    UNIVERSITY("university"),
    @SerializedName("medkit")
    MEDKIT("medkit"),
    @SerializedName("paw")
    PAW("paw"),
    @SerializedName("tree")
    TREE("tree"),
    @SerializedName("utensils")
    UTENSILS("utensils"),
    @SerializedName("male")
    MALE("male"),
    @SerializedName("female")
    FEMALE("female"),
    @SerializedName("life ring outline")
    LIFE_RING_OUTLINE("life ring outline");

    private String iconName;

    Icon(String s) {
        this.iconName = s;
    }

    @Override
    public String toString() {
        return iconName;
    }
}
