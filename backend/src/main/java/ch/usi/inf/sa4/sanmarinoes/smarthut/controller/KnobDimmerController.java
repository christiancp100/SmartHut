package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.KnobDimmerDimRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Dimmable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.KnobDimmer;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.KnobDimmerRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/knobDimmer")
public class KnobDimmerController extends InputDeviceConnectionController<KnobDimmer, Dimmable> {

    private final DeviceService deviceService;
    private final KnobDimmerRepository knobDimmerRepository;

    @Autowired
    protected KnobDimmerController(
            KnobDimmerRepository inputRepository,
            DimmableRepository<Dimmable> outputRepository,
            DeviceService deviceService) {
        super(inputRepository, outputRepository);
        this.knobDimmerRepository = inputRepository;
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public KnobDimmer findById(@PathVariable("id") long id) throws NotFoundException {
        return knobDimmerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public KnobDimmer create(
            @Valid @RequestBody GenericDeviceSaveRequest kd, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(kd.getRoomId(), principal.getName());
        KnobDimmer newKD = new KnobDimmer();
        newKD.setName(kd.getName());
        newKD.setRoomId(kd.getRoomId());

        return deviceService.saveAsOwner(newKD, principal.getName());
    }

    @PutMapping("/dimTo")
    public Set<Dimmable> dimTo(
            @Valid @RequestBody final KnobDimmerDimRequest bd, final Principal principal)
            throws NotFoundException {
        final KnobDimmer dimmer =
                knobDimmerRepository
                        .findByIdAndUsername(bd.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new);

        dimmer.setLightIntensity(bd.getIntensity());
        deviceService.saveAllAsOwner(dimmer.getOutputs(), principal.getName());

        return dimmer.getOutputs();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }
}
