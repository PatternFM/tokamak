package fm.pattern.tokamak.server.conversion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.service.AudienceService;
import fm.pattern.tokamak.server.service.AuthorityService;
import fm.pattern.tokamak.server.service.GrantTypeService;
import fm.pattern.tokamak.server.service.ScopeService;

@Service
public class ClientConversionService {

	private final ScopeConversionService scopeConversionService;
	private final GrantTypeConversionService grantTypeConversionService;
	private final AuthorityConversionService authorityConversionService;
	private final AudienceConversionService audienceConversionService;

	private final AuthorityService authorityService;
	private final AudienceService audienceService;
	private final ScopeService scopeService;
	private final GrantTypeService grantTypeService;

	@Autowired
	public ClientConversionService(ScopeConversionService scopeConversionService, GrantTypeConversionService grantTypeConversionService, AuthorityConversionService authorityConversionService, AudienceConversionService audienceConversionService, AuthorityService authorityService, AudienceService audienceService, ScopeService scopeService, GrantTypeService grantTypeService) {
		this.scopeConversionService = scopeConversionService;
		this.grantTypeConversionService = grantTypeConversionService;
		this.authorityConversionService = authorityConversionService;
		this.audienceConversionService = audienceConversionService;

		this.authorityService = authorityService;
		this.audienceService = audienceService;
		this.scopeService = scopeService;
		this.grantTypeService = grantTypeService;
	}

	public ClientRepresentation convert(Client client) {
		ClientRepresentation representation = new ClientRepresentation();

		representation.setId(client.getId());
		representation.setCreated(client.getCreated());
		representation.setUpdated(client.getUpdated());
		representation.setClientId(client.getClientId());
		representation.setName(client.getName());

		representation.setAudiences(client.getAudiences().stream().map(audience -> audienceConversionService.convert(audience)).collect(Collectors.toCollection(HashSet::new)));
		representation.setAuthorities(client.getAuthorities().stream().map(authority -> authorityConversionService.convert(authority)).collect(Collectors.toCollection(HashSet::new)));
		representation.setGrantTypes(client.getGrantTypes().stream().map(grantType -> grantTypeConversionService.convert(grantType)).collect(Collectors.toCollection(HashSet::new)));
		representation.setScopes(client.getScopes().stream().map(scope -> scopeConversionService.convert(scope)).collect(Collectors.toCollection(HashSet::new)));

		representation.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		representation.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());

		return representation;
	}

	public Client convert(ClientRepresentation representation) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : new HashSet<Scope>(scopeService.findExistingById(representation.getScopes().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : new HashSet<GrantType>(grantTypeService.findExistingById(representation.getGrantTypes().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : new HashSet<Authority>(authorityService.findExistingById(representation.getAuthorities().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<Audience> audiences = representation.getAudiences() == null ? new HashSet<Audience>() : new HashSet<Audience>(audienceService.findExistingById(representation.getAudiences().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());

		Client client = new Client(representation.getClientId(), representation.getClientSecret(), authorities, audiences, grantTypes, scopes);

		client.setName(representation.getName());
		client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());

		return client;

	}

	public Client convert(ClientRepresentation representation, Client client) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : new HashSet<Scope>(scopeService.findExistingById(representation.getScopes().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : new HashSet<GrantType>(grantTypeService.findExistingById(representation.getGrantTypes().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : new HashSet<Authority>(authorityService.findExistingById(representation.getAuthorities().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());
		Set<Audience> audiences = representation.getAudiences() == null ? new HashSet<Audience>() : new HashSet<Audience>(audienceService.findExistingById(representation.getAudiences().stream().map(i -> i.getId()).collect(Collectors.toList())).getInstance());

		client.setScopes(scopes);
		client.setGrantTypes(grantTypes);
		client.setAuthorities(authorities);
		client.setAudiences(audiences);

		client.setName(representation.getName());
		client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());

		return client;
	}

}
