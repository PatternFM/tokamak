package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.AuthorityDSL.authority;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ResultType;

public class AuthorityServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AuthorityService authorityService;

	@Test
	public void shouldBeAbleToCreateAAuthority() {
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
	public void shouldNotBeAbleToCreateAAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = new Authority(null);

		Result<Authority> result = authorityService.create(authority);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToUpdateAAuthority() {
		Authority authority = authority().thatIs().persistent().build();
		authority.setName("first");

		Result<Authority> result = authorityService.update(authority);
		assertThat(result).accepted();

		Authority found = authorityService.findById(authority.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = authority().thatIs().persistent().build();
		authority.setName(null);

		Result<Authority> result = authorityService.update(authority);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToDeleteAAuthority() {
		Authority authority = authority().thatIs().persistent().build();
		assertThat(authorityService.findById(authority.getId())).accepted();

		Result<Authority> result = authorityService.delete(authority);
		assertThat(result).accepted();

		assertThat(authorityService.findById(authority.getId())).rejected().withType(ResultType.NOT_FOUND);
	}

	@Test
	public void shouldBeAbleToFindAAuthorityById() {
		Authority authority = authority().thatIs().persistent().build();

		Result<Authority> result = authorityService.findById(authority.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdIsNullOrEmpty() {
		assertThat(authorityService.findById(null)).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority id to retrieve cannot be null or empty.");
		assertThat(authorityService.findById("")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority id to retrieve cannot be null or empty.");
		assertThat(authorityService.findById("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdDoesNotExist() {
		assertThat(authorityService.findById("csrx")).rejected().withType(NOT_FOUND).withDescription("No such authority id: csrx");
	}

	@Test
	public void shouldBeAbleToFindAAuthorityByName() {
		Authority authority = authority().thatIs().persistent().build();

		Result<Authority> result = authorityService.findByName(authority.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAAuthorityByNameIfTheAuthorityNameIsNullOrEmpty() {
		assertThat(authorityService.findByName(null)).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority name to retrieve cannot be null or empty.");
		assertThat(authorityService.findByName("")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority name to retrieve cannot be null or empty.");
		assertThat(authorityService.findByName("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The authority name to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAAuthorityByNameIfTheAuthorityNameDoesNotExist() {
		assertThat(authorityService.findByName("csrx")).rejected().withType(NOT_FOUND).withDescription("No such authority name: csrx");
	}

	@Test
	public void shouldBeAbleToListAllAuthorities() {
		range(0, 5).forEach(i -> authority().thatIs().persistent().build());

		Result<List<Authority>> result = authorityService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
