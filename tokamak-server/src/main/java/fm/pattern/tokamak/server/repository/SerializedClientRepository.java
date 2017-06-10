package fm.pattern.tokamak.server.repository;

import fm.pattern.tokamak.server.model.SerializedClient;
import fm.pattern.valex.Result;

public interface SerializedClientRepository extends DataRepository {

	Result<SerializedClient> findById(String id);

	Result<SerializedClient> findByClientId(String clientId);

}
