package fm.pattern.tokamak.server.conversion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.service.AudienceService;
import fm.pattern.tokamak.server.service.AuthorityService;
import fm.pattern.tokamak.server.service.GrantTypeService;
import fm.pattern.tokamak.server.service.ScopeService;
import fm.pattern.valex.Result;

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
		representation.setAudiences(client.getAudiences().stream().map(audience -> audienceConversionService.convert(audience)).collect(Collectors.toCollection(HashSet::new)));
		representation.setAuthorities(client.getAuthorities().stream().map(authority -> authorityConversionService.convert(authority)).collect(Collectors.toCollection(HashSet::new)));
		representation.setGrantTypes(client.getGrantTypes().stream().map(grantType -> grantTypeConversionService.convert(grantType)).collect(Collectors.toCollection(HashSet::new)));
		representation.setScopes(client.getScopes().stream().map(scope -> scopeConversionService.convert(scope)).collect(Collectors.toCollection(HashSet::new)));
		representation.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		representation.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
		return representation;
	}

	public Client convert(ClientRepresentation representation) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());
		Set<Audience> audiences = representation.getAudiences() == null ? new HashSet<Audience>() : representation.getAudiences().stream().map(aud -> lookup(aud)).filter(aud -> aud != null).collect(Collectors.toSet());

		Client client = new Client(representation.getClientId(), representation.getClientSecret(), authorities, audiences, grantTypes, scopes);

		if (representation.getAccessTokenValiditySeconds() != null) {
			client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		}
		if (representation.getRefreshTokenValiditySeconds() != null) {
			client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());
		}

		return client;

	}

	public Client convert(ClientRepresentation representation, Client client) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());

		client.setScopes(scopes);
		client.setGrantTypes(grantTypes);
		client.setAuthorities(authorities);

		client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());

		return client;
	}

	private Audience lookup(AudienceRepresentation representation) {
		Result<Audience> result = audienceService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Authority lookup(AuthorityRepresentation representation) {
		Result<Authority> result = authorityService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private GrantType lookup(GrantTypeRepresentation representation) {
		Result<GrantType> result = grantTypeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Scope lookup(ScopeRepresentation representation) {
		Result<Scope> result = scopeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

}
