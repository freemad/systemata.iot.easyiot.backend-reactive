package systemata.iot.eiot.common.contracts.domain;

import java.io.Serializable;

public interface IErrorCode extends Serializable {

    int getCode();

    String getMessage();

    String getDescription();
}
