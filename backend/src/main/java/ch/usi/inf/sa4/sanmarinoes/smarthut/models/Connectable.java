package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

public interface Connectable<O extends OutputDevice> {
    void connect(O output, boolean connect);
}
