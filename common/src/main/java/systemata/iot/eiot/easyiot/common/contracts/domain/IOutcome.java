package systemata.iot.eiot.easyiot.common.contracts.domain;

public interface IOutcome<R extends IModel, E extends IError> {
    R getResult();

    Class<R> getResultClass();

    E getError();

    Class<E> getErrorClass();

    default boolean isSuccess() {
        return getError() == null;
    }
}
