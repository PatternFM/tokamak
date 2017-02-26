package fm.pattern.jwt.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.commons.util.ReflectionUtils;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.repository.ClientRepository;
import fm.pattern.jwt.server.security.PasswordEncodingService;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Create;

@Service
class ClientServiceImpl extends DataServiceImpl<Client> implements ClientService {

	private PasswordEncodingService passwordEncodingService;
	private ValidationService validationService;
	private ClientRepository clientRepository;

	@Transactional(readOnly = false)
	public Result<Client> create(Client client) {
		Result<Client> result = validationService.validate(client, Create.class);
		if (result.rejected()) {
			return result;
		}
		ReflectionUtils.setValue(client, "password", passwordEncodingService.encode(client.getPassword()));
		return clientRepository.save(client);
	}

	@Transactional(readOnly = true)
	public Result<Client> findById(String id) {
		return super.findById(id, Client.class);
	}

	@Transactional(readOnly = true)
	public Result<Client> findByUsername(String username) {
		if (isBlank(username)) {
			return Result.reject("{client.get.username.required}");
		}

		Client client = clientRepository.findByUsername(username);
		return client != null ? Result.accept(client) : Result.not_found("No such username: " + username);
	}

	@Autowired
	public void setPasswordEncodingService(PasswordEncodingService passwordEncodingService) {
		this.passwordEncodingService = passwordEncodingService;
	}

	@Autowired
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	@Autowired
	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

}
