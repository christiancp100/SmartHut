package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Connectable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DeviceRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.InputDevice;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.OutputDevice;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * An abstract controller for an input device that has output connected to it. Aids to create the
 * output add and output remove route
 *
 * @param <I> the type of device this controller is for
 * @param <O> the output device attached to I
 */
public abstract class InputDeviceConnectionController<
        I extends InputDevice & Connectable<O>, O extends OutputDevice> {

    private class Connection {
        private final I input;
        private final List<O> outputs;

        private Connection(I input, List<O> outputs) {
            this.input = input;
            this.outputs = outputs;
        }

        public I getInput() {
            return input;
        }

        public List<O> getOutputs() {
            return outputs;
        }
    }

    protected DeviceRepository<I> getInputRepository() {
        return inputRepository;
    }

    protected DeviceRepository<O> getOutputReposiory() {
        return outputReposiory;
    }

    @Autowired private DeviceService deviceService;

    private final DeviceRepository<I> inputRepository;

    private final DeviceRepository<O> outputReposiory;

    /**
     * Contstructs the controller by requiring essential object for the controller implementation
     *
     * @param inputRepository the input device repository
     * @param outputRepository the output device repository
     */
    protected InputDeviceConnectionController(
            DeviceRepository<I> inputRepository, DeviceRepository<O> outputRepository) {
        this.inputRepository = inputRepository;
        this.outputReposiory = outputRepository;
    }

    private Connection checkConnectionIDs(Long inputId, List<Long> outputs, String username)
            throws NotFoundException {
        final I input =
                inputRepository
                        .findByIdAndUsername(inputId, username)
                        .orElseThrow(() -> new NotFoundException("input device"));
        final List<O> outputDevices = new ArrayList<>(outputs.size());
        for (final Long outputId : outputs) {
            outputDevices.add(
                    outputReposiory
                            .findByIdAndUsername(outputId, username)
                            .orElseThrow(() -> new NotFoundException("output device")));
        }
        return new Connection(input, outputDevices);
    }

    /**
     * Implements the output device connection creation (add) route
     *
     * @param inputId input device id
     * @param outputs output device id list
     * @return the list of output devices attached to the input device of id inputId
     * @throws NotFoundException if inputId or outputId are not valid
     */
    protected Set<? extends OutputDevice> addOutput(
            Long inputId, List<Long> outputs, String username) throws NotFoundException {
        final Connection pair = checkConnectionIDs(inputId, outputs, username);

        for (final O o : pair.getOutputs()) {
            pair.getInput().connect(o, true);
        }

        deviceService.saveAllAsOwner(pair.getOutputs(), username);
        return pair.getInput().getOutputs();
    }

    /**
     * Implements the output device connection destruction (remove) route
     *
     * @param inputId input device id
     * @param outputs output device id list
     * @return the list of output devices attached to the input device of id inputId
     * @throws NotFoundException if inputId or outputId are not valid
     */
    protected Set<? extends OutputDevice> removeOutput(
            Long inputId, List<Long> outputs, String username) throws NotFoundException {
        final Connection pair = checkConnectionIDs(inputId, outputs, username);

        for (final O o : pair.getOutputs()) {
            pair.getInput().connect(o, false);
        }

        deviceService.saveAllAsOwner(pair.getOutputs(), username);
        return pair.getInput().getOutputs();
    }

    @PostMapping("/{id}/lights")
    public List<OutputDevice> addLight(
            @PathVariable("id") long inputId,
            @RequestBody List<Long> lightId,
            final Principal principal)
            throws NotFoundException {
        return toList(addOutput(inputId, lightId, principal.getName()));
    }

    @DeleteMapping("/{id}/lights")
    public List<OutputDevice> removeLight(
            @PathVariable("id") long inputId,
            @RequestBody List<Long> lightId,
            final Principal principal)
            throws NotFoundException {
        return toList(removeOutput(inputId, lightId, principal.getName()));
    }
}
