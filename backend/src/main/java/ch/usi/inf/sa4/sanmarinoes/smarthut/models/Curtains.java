package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Entity;

/**
 * Represents a curtain. The intensity represents how much the curtains are opened, 0 is completely
 * closed 100 is completely open
 */
@Entity
public class Curtains extends Dimmable {
    public Curtains() {
        super("curtains");
    }
}
