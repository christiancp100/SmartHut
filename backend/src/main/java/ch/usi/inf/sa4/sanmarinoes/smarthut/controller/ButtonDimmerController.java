package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ButtonDimmerDimRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ButtonDimmer;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ButtonDimmerRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Dimmable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/buttonDimmer")
public class ButtonDimmerController
        extends InputDeviceConnectionController<ButtonDimmer, Dimmable> {

    private DeviceService deviceService;
    private ButtonDimmerRepository buttonDimmerRepository;

    @Autowired
    protected ButtonDimmerController(
            ButtonDimmerRepository inputRepository,
            DimmableRepository<Dimmable> outputRepository,
            DeviceService deviceService) {
        super(inputRepository, outputRepository);
        this.deviceService = deviceService;
        this.buttonDimmerRepository = inputRepository;
    }

    @GetMapping("/{id}")
    public ButtonDimmer findById(@PathVariable("id") long id) throws NotFoundException {
        return buttonDimmerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ButtonDimmer create(
            @Valid @RequestBody final GenericDeviceSaveRequest bd, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(bd.getRoomId(), principal.getName());

        ButtonDimmer newBD = new ButtonDimmer();
        newBD.setName(bd.getName());
        newBD.setRoomId(bd.getRoomId());

        return deviceService.saveAsOwner(newBD, principal.getName());
    }

    @PutMapping("/dim")
    public Set<Dimmable> dim(
            @Valid @RequestBody final ButtonDimmerDimRequest bd, final Principal principal)
            throws NotFoundException {
        final ButtonDimmer buttonDimmer =
                buttonDimmerRepository
                        .findByIdAndUsername(bd.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new);

        if (bd.getDimType() == ButtonDimmerDimRequest.DimType.UP) {

            buttonDimmer.increaseIntensity();
        } else {
            buttonDimmer.decreaseIntensity();
        }

        deviceService.saveAllAsOwner(buttonDimmer.getOutputs(), principal.getName());

        return buttonDimmer.getOutputs();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }
}
