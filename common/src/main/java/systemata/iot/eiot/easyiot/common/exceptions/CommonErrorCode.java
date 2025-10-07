package systemata.iot.eiot.easyiot.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systemata.iot.eiot.easyiot.common.contracts.domain.IErrorCode;

import java.util.List;
import java.util.stream.Stream;

import static systemata.iot.eiot.easyiot.common.domain.constants.Global.INITIAL_ERROR_CODE_COMMON;


@Getter
@AllArgsConstructor
public enum CommonErrorCode implements IErrorCode {

    /******* GENERAL EXCEPTION *******/
    GENERAL_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 1, "GENERAL_EXCEPTION", "general exception"),
    /******* INFRA-BASED EXCEPTIONS *******/
    CONNECTION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 2, "CONNECTION_EXCEPTION", "connection exception"),
    INFRASTRUCTURE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 3, "INFRASTRUCTURE_EXCEPTION", "infrastructure exception"),
    /******* SECURITY-BASED EXCEPTIONS *******/
    AUTHENTICATION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 4, "AUTHENTICATION_EXCEPTION", "authentication exception"),
    AUTHORIZATION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 5, "AUTHORIZATION_EXCEPTION", "authorization exception"),
    /******* INPUT EXCEPTIONS *******/
    PROTOCOL_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 6, "PROTOCOL_EXCEPTION", "protocol exception"),
    METHOD_VALIDATION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 7, "METHOD_VALIDATION_EXCEPTION", "method validation exception"),
    ILLEGAL_FORMAT_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 8, "ILLEGAL_FORMAT_EXCEPTION", "illegal format exception"),
    ILLEGAL_ARGUMENT_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 9, "ILLEGAL_ARGUMENT_EXCEPTION", "illegal argument exception"),
    DATA_VALIDATION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 10, "DATA_VALIDATION_EXCEPTION", "data validation exception"),
    BAD_REQUEST_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 11, "BAD_REQUEST_EXCEPTION", "bad request exception"),
    /******* PROCESSING EXCEPTIONS *******/
    ACCESS_VIOLATION_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 12, "ACCESS_VIOLATION_EXCEPTION", "access violation exception"),
    PARSE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 13, "PARSE_EXCEPTION", "parse exception"),
    NULL_POINTER_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 14, "NULL_POINTER_EXCEPTION", "null pointer exception"),
    PROCESSING_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 15, "PROCESSING_EXCEPTION", "processing exception"),
    MEMORY_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 16, "MEMORY_EXCEPTION", "memory exception"),
    ABORTED_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 17, "ABORTED_EXCEPTION", "aborted exception"),
    CONCURRENCY_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 18, "CONCURRENCY_EXCEPTION", "concurrency exception"),
    DATA_RETRIEVE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 19, "DATA_RETRIEVE_EXCEPTION", "data retrieve exception"),
    DATA_TRANSFER_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 20, "DATA_TRANSFER_EXCEPTION", "data transfer exception"),
    DATA_PERSISTENCE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 21, "DATA_PERSISTENCE_EXCEPTION", "data persistence exception"),
    NOT_FOUND_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 22, "NOT_FOUND_EXCEPTION", "not found exception"),
    EVENT_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 23, "EVENT_EXCEPTION", "event exception"),
    LIFECYCLE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 24, "LIFECYCLE_EXCEPTION", "lifecycle exception"),
    TIME_BOX_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 25, "TIME_BOX_EXCEPTION", "time-box exception"),
    NOT_IMPLEMENTED_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 26, "NOT_IMPLEMENTED_EXCEPTION", "not implemented exception"),
    INTERNAL_SERVICE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 27, "INTERNAL_SERVICE_EXCEPTION", "internal service exception"),
    GATEWAY_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 28, "GATEWAY_EXCEPTION", "gateway exception"),
    RESOURCE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 29, "RESOURCE_EXCEPTION", "resource exception"),
    EXTERNAL_SERVICE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 30, "EXTERNAL_SERVICE_EXCEPTION", "external service exception"),
    REMOTE_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 31, "REMOTE_EXCEPTION", "remote exception"),
    /******* UNKNOWN EXCEPTION *******/
    UNKNOWN_EXCEPTION(INITIAL_ERROR_CODE_COMMON + 99, "UNKNOWN_EXCEPTION", "unknown exception");

    private final int code;
    private final String message;
    private final String description;

    public static List<CommonErrorCode> asList() {
        return Stream.of(CommonErrorCode.values())
                .toList();
    }
}
