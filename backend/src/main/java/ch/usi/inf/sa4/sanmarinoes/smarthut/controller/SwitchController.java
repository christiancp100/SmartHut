package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchOperationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switch;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switchable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchableRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/switch")
public class SwitchController extends InputDeviceConnectionController<Switch, Switchable> {

    private final SwitchRepository switchRepository;
    private final DeviceService deviceService;

    /**
     * Contstructs the controller by requiring essential object for the controller implementation
     *
     * @param inputRepository the input device repository
     * @param outputRepository the output device repository
     */
    @Autowired
    protected SwitchController(
            SwitchRepository inputRepository,
            SwitchableRepository<Switchable> outputRepository,
            DeviceService deviceService) {
        super(inputRepository, outputRepository);
        this.deviceService = deviceService;
        this.switchRepository = inputRepository;
    }

    @GetMapping("/{id}")
    public Switch findById(@PathVariable("id") long id) throws NotFoundException {
        return switchRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Switch create(@Valid @RequestBody GenericDeviceSaveRequest s, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(s.getRoomId(), principal.getName());
        Switch newSwitch = new Switch();
        newSwitch.setName(s.getName());
        newSwitch.setRoomId(s.getRoomId());

        return deviceService.saveAsOwner(newSwitch, principal.getName());
    }

    @PutMapping("/operate")
    public List<Switchable> operate(
            @Valid @RequestBody final SwitchOperationRequest sr, final Principal principal)
            throws NotFoundException {
        final Switch s =
                switchRepository
                        .findByIdAndUsername(sr.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new);

        switch (sr.getType()) {
            case ON:
                s.setOn(true);
                break;
            case OFF:
                s.setOn(false);
                break;
            case TOGGLE:
                s.toggle();
                break;
        }

        deviceService.saveAsOwner(s, principal.getName());
        return deviceService.saveAllAsOwner(s.getOutputs(), principal.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }
}
