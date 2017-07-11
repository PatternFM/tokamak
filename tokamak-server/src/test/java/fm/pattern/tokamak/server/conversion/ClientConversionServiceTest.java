package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.AudienceDSL.audience;
import static fm.pattern.tokamak.server.dsl.AuthorityDSL.authority;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.tokamak.server.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.ClientDSL;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Scope;

public class ClientConversionServiceTest extends IntegrationTest {

	@Autowired
	private ClientConversionService clientConversionService;

	@Autowired
	private AudienceConversionService audienceConversionService;

	@Autowired
	private AuthorityConversionService authorityConversionService;

	@Autowired
	private ScopeConversionService scopeConversionService;

	@Autowired
	private GrantTypeConversionService grantTypeConversionService;

	@Test
	public void shouldBeAbleToConvertAClientIntoAClientRepresentation() {
		Authority authority = authority().build();
		Audience audience = audience().build();
		Scope scope = scope().build();
		GrantType grantType = grantType().build();

		Client client = client().withAudience(audience).withDescription("description").withAuthority(authority).withScope(scope).withGrantType(grantType).build();

		ClientRepresentation representation = clientConversionService.convert(client);
		assertThat(representation.getId()).isEqualTo(client.getId());
		assertThat(representation.getCreated()).isEqualTo(client.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(client.getUpdated());
		assertThat(representation.getClientId()).isEqualTo(client.getClientId());
		assertThat(representation.getClientSecret()).isNull();
		assertThat(representation.getDescription()).isEqualTo("description");
		assertThat(representation.getAccessTokenValiditySeconds()).isEqualTo(client.getAccessTokenValiditySeconds());
		assertThat(representation.getRefreshTokenValiditySeconds()).isEqualTo(client.getRefreshTokenValiditySeconds());

		assertThat(representation.getAudiences()).hasSize(1);
		assertThat(representation.getAuthorities()).hasSize(1);
		assertThat(representation.getScopes()).hasSize(1);
		assertThat(representation.getGrantTypes()).hasSize(1);
	}

	@Test
	public void shouldBeAbleToConvertAClientRepresentationIntoANewClient() {
		Authority authority = authority().save();
		Audience audience = audience().save();
		Scope scope = scope().save();
		GrantType grantType = grantType().save();

		ClientRepresentation representation = ClientDSL.client().withDescription("desc").withAudiences(audienceConversionService.convert(audience)).withAuthorities(authorityConversionService.convert(authority)).withScopes(scopeConversionService.convert(scope)).withGrantTypes(grantTypeConversionService.convert(grantType)).build();

		Client client = clientConversionService.convert(representation);
		assertThat(client.getAuthorities()).hasSize(1);
		assertThat(client.getAuthorities()).contains(authority);
		assertThat(client.getAudiences()).hasSize(1);
		assertThat(client.getAudiences()).contains(audience);
		assertThat(client.getGrantTypes()).hasSize(1);
		assertThat(client.getGrantTypes()).contains(grantType);
		assertThat(client.getScopes()).hasSize(1);
		assertThat(client.getScopes()).contains(scope);
		assertThat(client.getDescription()).isEqualTo("desc");

		assertThat(client.getAccessTokenValiditySeconds()).isEqualTo(representation.getAccessTokenValiditySeconds());
		assertThat(client.getRefreshTokenValiditySeconds()).isEqualTo(representation.getRefreshTokenValiditySeconds());
		assertThat(client.getClientId()).isEqualTo(representation.getClientId());
		assertThat(client.getClientSecret()).isEqualTo(representation.getClientSecret());
	}

	@Test
	public void shouldBeAbleToConvertAClientRepresentationIntoAnExistingClient() {
		Authority authority = authority().save();
		Audience audience = audience().save();
		Scope scope = scope().save();
		GrantType grantType = grantType().save();

		Client client = client().withDescription("desc").withGrantType(grantType).save();
		ClientRepresentation representation = ClientDSL.client().withAudiences(audienceConversionService.convert(audience)).withAuthorities(authorityConversionService.convert(authority)).withScopes(scopeConversionService.convert(scope)).withGrantTypes(grantTypeConversionService.convert(grantType)).build();

		Client updated = clientConversionService.convert(representation, client);

		assertThat(updated.getId()).isEqualTo(client.getId());
		assertThat(updated.getCreated()).isEqualTo(client.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(client.getUpdated());
		assertThat(updated.getClientId()).isEqualTo(client.getClientId());
		assertThat(updated.getClientSecret()).isEqualTo(client.getClientSecret());
		assertThat(updated.getName()).isEqualTo(client.getName());

		assertThat(updated.getAuthorities()).hasSize(1);
		assertThat(updated.getAuthorities()).contains(authority);
		assertThat(updated.getAudiences()).hasSize(1);
		assertThat(updated.getAudiences()).contains(audience);
		assertThat(updated.getGrantTypes()).hasSize(1);
		assertThat(updated.getGrantTypes()).contains(grantType);
		assertThat(updated.getScopes()).hasSize(1);
		assertThat(updated.getScopes()).contains(scope);

		assertThat(updated.getRedirectUri()).isEqualTo(representation.getRedirectUri());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
		assertThat(updated.getAccessTokenValiditySeconds()).isEqualTo(representation.getAccessTokenValiditySeconds());
		assertThat(updated.getRefreshTokenValiditySeconds()).isEqualTo(representation.getRefreshTokenValiditySeconds());
	}

}
