package systemata.iot.eiot.easyiot.core.domain.entities;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntity;
import systemata.iot.eiot.easyiot.common.domain.enums.DeviceEventType;

import java.io.Serial;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "device_data")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataEntity
        implements IEntity<String> {
    @Serial
    private static final long serialVersionUID = -360054074677671020L;

    @Id
    private String id; // generated as relatedId + "_" + eventType + "_" + ts (helper in service layer)

    @Column("related_id")
    private UUID relatedId;

    @Column("ts")
    private Instant ts;

    @Column("event_type")
    private Short eventType;

    @Column("event_source")
    private Short eventSource;

    @Column("data")
    private Double data;

    @Column("meta_data")
    private Map<String, Object> metaData;

    public DeviceDataEntity(UUID relatedId, Instant ts, Short eventType, Short eventSource, Double data) {
        this.relatedId = relatedId;
        this.ts = ts;
        this.data = data;
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.id = relatedId + "_" + eventType + "_" + ts.toEpochMilli(); // synthetic key
    }

    @Transient
    public String getRelatedType() {
        return eventType.equals(DeviceEventType.DEVICE_EVENT_TYPE_COMMAND.getValue())
                ? "device"
                : "gate";
    }
}
