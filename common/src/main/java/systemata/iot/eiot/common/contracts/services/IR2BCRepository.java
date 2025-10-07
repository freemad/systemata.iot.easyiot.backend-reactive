package systemata.iot.eiot.common.contracts.services;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import systemata.iot.eiot.common.contracts.domain.IEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface IR2BCRepository <T extends IEntity<I>, I extends Serializable>
        extends ReactiveCrudRepository<T, I> {

}
