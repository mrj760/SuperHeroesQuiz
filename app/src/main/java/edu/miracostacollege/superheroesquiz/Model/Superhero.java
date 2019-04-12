package edu.miracostacollege.superheroesquiz.Model;

public class Superhero {

    private String name;
    private String thingToKnow;
    private String superpower;
    private String imageName;

    public Superhero(String name, String thingToKnow, String superpower, String imageName) {
        this.name = name;
        this.thingToKnow = thingToKnow;
        this.superpower = superpower;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public String getThingToKnow() {
        return thingToKnow;
    }

    public String getSuperpower() {
        return superpower;
    }

    public String getImageName() {
        return imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThingToKnow(String thingToKnow) {
        this.thingToKnow = thingToKnow;
    }

    public void setSuperpower(String superpower) {
        this.superpower = superpower;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
