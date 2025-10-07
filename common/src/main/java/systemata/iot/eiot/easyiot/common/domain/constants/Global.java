package systemata.iot.eiot.easyiot.common.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Global {

    public static final int APP_ERROR_CODE_PREFIX = 10000;
    public static final int INITIAL_ERROR_CODE_COMMON = APP_ERROR_CODE_PREFIX + 100;
}
