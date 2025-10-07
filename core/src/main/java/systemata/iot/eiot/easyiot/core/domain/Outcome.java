package systemata.iot.eiot.easyiot.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IError;
import systemata.iot.eiot.easyiot.common.contracts.domain.IModel;
import systemata.iot.eiot.easyiot.common.contracts.domain.IOutcome;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Outcome<R extends IModel, E extends IError>
        implements IOutcome<R, E> {

    private R result;
    private Class<R> resultClass;
    private E error;
    private Class<E> errorClass;
}
