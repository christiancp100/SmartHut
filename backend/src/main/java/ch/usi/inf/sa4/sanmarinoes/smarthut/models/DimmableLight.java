package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Entity;

/** Represent a dimmable light */
@Entity
public class DimmableLight extends Dimmable {
    public DimmableLight() {
        super("dimmableLight");
    }
}
