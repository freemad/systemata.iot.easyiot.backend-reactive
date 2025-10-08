package systemata.iot.eiot.easyiot.common.exceptions;

import lombok.Getter;
import systemata.iot.eiot.easyiot.common.contracts.domain.IBusinessException;
import systemata.iot.eiot.easyiot.common.contracts.domain.IErrorCode;

import java.io.Serial;

@Getter
public class BusinessException
        extends RuntimeException
        implements IBusinessException {
    @Serial
    private static final long serialVersionUID = 5882376065584899341L;
    private static final String BUSINESS_EXCEPTION_KEY = "BusinessException";

    private final IErrorCode errorCode;
    private final String additionalInfo;
    private final Throwable cause;
    private final boolean enableSuppression;
    private final boolean writableStackTrace;


    public BusinessException(IErrorCode errorCode) {
        this(errorCode, "", null, false, true);
    }

    public BusinessException(IErrorCode errorCode, String additionalInfo) {
        this(errorCode, additionalInfo, null, false, true);
    }

    public BusinessException(IErrorCode errorCode, String additionalInfo, Throwable cause) {
        this(errorCode, additionalInfo, cause, false, true);
    }

    public BusinessException(IErrorCode errorCode, String additionalInfo, boolean enableSuppression, boolean writableStackTrace) {
        this(errorCode, additionalInfo, null, enableSuppression, writableStackTrace);
    }

    protected BusinessException(
            IErrorCode errorCode,
            String additionalInfo,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
            ) {
        super(errorCode.getMessage(), cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.additionalInfo = additionalInfo;
        this.cause = cause;
        this.enableSuppression = enableSuppression;
        this.writableStackTrace = writableStackTrace;
    }
}
