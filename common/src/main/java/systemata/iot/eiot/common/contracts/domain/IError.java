package systemata.iot.eiot.common.contracts.domain;

import java.util.Map;

public interface IError
        extends IModel {
    Integer getCode();

    String getMessage();

    Map<String, String> getParams();
}
