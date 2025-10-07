package systemata.iot.eiot.core.domain.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import systemata.iot.eiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.time.Instant;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class GateData
        implements IEntityModel<String> {
    @Serial
    private static final long serialVersionUID = -1544678960300055015L;

    private String id;
    private Gate gate;
    private Instant ts;
    private Double value;
}
