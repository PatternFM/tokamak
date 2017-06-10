package fm.pattern.tokamak.server.repository;

import javax.persistence.NoResultException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.SerializedClient;
import fm.pattern.valex.Result;

@Repository
class SerializedClientRepositoryImpl extends DataRepositoryImpl implements SerializedClientRepository {

	@Transactional(readOnly = true)
	public Result<SerializedClient> findById(String id) {
		try {
			return Result.accept((SerializedClient) super.query("from SerializedClients client where client.id = :id").setParameter("id", id).getSingleResult());
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return Result.reject("system.not.found", "client", id);
		}
	}

	@Transactional(readOnly = true)
	public Result<SerializedClient> findByClientId(String clientId) {
		try {
			return Result.accept((SerializedClient) super.query("from SerializedClients client where client.clientId = :clientId").setParameter("clientId", clientId).getSingleResult());
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return Result.reject("client.clientId.not_found", clientId);
		}
	}

}
