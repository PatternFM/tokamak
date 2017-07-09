package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.AuthorityDSL.authority;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class AuthorityServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	@Qualifier("clientCache")
	private Cache cache;

	@Test
	public void shouldBeAbleToCreateAnAuthority() {
		Authority authority = new Authority("user");

		Result<Authority> result = authorityService.create(authority);
		assertThat(result).accepted();

		Authority created = result.getInstance();
		assertThat(created.getName()).isEqualTo(authority.getName());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = authority().withName(null).build();
		assertThat(authorityService.create(authority)).rejected().withMessage("An authority name is required.");
	}

	@Test
	public void shouldBeAbleToUpdateAnAuthority() {
		Authority authority = authority().save();
		authority.setName("first");

		assertThat(authorityService.update(authority)).accepted();

		Authority found = authorityService.findById(authority.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldFlushTheClientCacheWhenAnAuthorityIsUpdated() {
		Client client = client().withGrantType(grantType().save()).save();

		assertThat(cache.get("clients:id:" + client.getId(), Client.class)).isNotNull();
		assertThat(authorityService.update(authority().save())).accepted();
		assertThat(cache.get("clients:id:" + client.getId(), Client.class)).isNull();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = authority().save();
		authority.setName(null);

		assertThat(authorityService.update(authority)).rejected().withMessage("An authority name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAnAuthority() {
		Authority authority = authority().save();
		assertThat(authorityService.findById(authority.getId())).accepted();

		assertThat(authorityService.delete(authority)).accepted();
		assertThat(authorityService.findById(authority.getId())).rejected();
	}

	@Test
	public void shouldNotBeAbleToDeleteAnAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = authority().save();
		authority.setId(null);
		assertThat(authorityService.delete(authority)).rejected().withError("ENT-0001", "An id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToDeleteAnAuthorityIfTheAuthorityIsBeingUsedByClients() {
		Authority authority = authority().save();
		client().withAuthority(authority).withGrantType(grantType().save()).save();

		Result<Authority> result = authorityService.delete(authority);
		assertThat(result).rejected().withError("ATH-0005", "This authority cannot be deleted, 1 client is linked to this authority.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToFindAnAuthorityById() {
		Authority authority = authority().save();

		Result<Authority> result = authorityService.findById(authority.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdIsNullOrEmpty() {
		assertThat(authorityService.findById(null)).rejected().withError("ATH-0006", "An authority id is required.", UnprocessableEntityException.class);
		assertThat(authorityService.findById("")).rejected().withError("ATH-0006", "An authority id is required.", UnprocessableEntityException.class);
		assertThat(authorityService.findById("  ")).rejected().withError("ATH-0006", "An authority id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdDoesNotExist() {
		assertThat(authorityService.findById("csrx")).rejected().withError("SYS-0001", "No such authority id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAnAuthorityByName() {
		Authority authority = authority().save();

		Result<Authority> result = authorityService.findByName(authority.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByNameIfTheAuthorityNameIsNullOrEmpty() {
		assertThat(authorityService.findByName(null)).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		assertThat(authorityService.findByName("")).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		assertThat(authorityService.findByName("  ")).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByNameIfTheAuthorityNameDoesNotExist() {
		assertThat(authorityService.findByName("csrx")).rejected().withError("ATH-0008", "No such authority name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllAuthorities() {
		range(0, 5).forEach(i -> authority().save());

		Result<List<Authority>> result = authorityService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

	@Test
	public void shouldBeAbleToFindMultipleAuthoritiesById() {
		Authority authority1 = authority().save();
		Authority authority2 = authority().save();
		Authority authority3 = authority().save();

		List<String> ids = new ArrayList<String>();
		ids.add(authority1.getId());
		ids.add(authority2.getId());
		ids.add(authority3.getId());

		Result<List<Authority>> result = authorityService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(authority1, authority2, authority3);
	}

	@Test
	public void shouldReturnAnEmptyListOfAuthoritiesIfTheAuthorityIdListIsNullOrEmpty() {
		assertThat(authorityService.findExistingById(null)).accepted();
		assertThat(authorityService.findExistingById(null).getInstance()).isEmpty();

		assertThat(authorityService.findExistingById(new ArrayList<String>())).accepted();
		assertThat(authorityService.findExistingById(new ArrayList<String>()).getInstance()).isEmpty();
	}

	@Test
	public void shouldIgnoreEmptyAuthorityEntries() {
		Authority authority1 = authority().save();
		Authority authority2 = authority().save();
		Authority authority3 = authority().save();

		List<String> ids = new ArrayList<>();
		ids.add(authority1.getId());
		ids.add(null);
		ids.add(authority2.getId());
		ids.add("");
		ids.add(authority3.getId());

		Result<List<Authority>> result = authorityService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(authority1, authority2, authority3);
	}

	@Test
	public void shouldReturnAnEmtpyListWhenAllAuthoritiesAreNullOrEmpty() {
		List<String> ids = new ArrayList<>();
		ids.add(null);
		ids.add("");

		Result<List<Authority>> result = authorityService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEmpty();
	}

}
