package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

/**
 * SwitchableRepository acts as a superclass for the other repositories so to mirror in the database
 * the class inheritance present among the various switchable devices.
 */
public interface SwitchableRepository<T extends Switchable> extends DeviceRepository<T> {}
