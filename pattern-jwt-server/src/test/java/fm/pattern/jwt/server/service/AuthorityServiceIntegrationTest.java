package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.AuthorityDSL.authority;
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
		Authority authority = new Authority(null);

		Result<Authority> result = authorityService.create(authority);
		assertThat(result).rejected();
	}

	@Test
	public void shouldBeAbleToUpdateAnAuthority() {
		Authority authority = authority().thatIs().persistent().build();
		authority.setName("first");

		Result<Authority> result = authorityService.update(authority);
		assertThat(result).accepted();

		Authority found = authorityService.findById(authority.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityIfTheAuthorityIsInvalid() {
		Authority authority = authority().thatIs().persistent().build();
		authority.setName(null);

		Result<Authority> result = authorityService.update(authority);
		assertThat(result).rejected();
	}

	@Test
	public void shouldBeAbleToDeleteAnAuthority() {
		Authority authority = authority().thatIs().persistent().build();
		assertThat(authorityService.findById(authority.getId())).accepted();

		Result<Authority> result = authorityService.delete(authority);
		assertThat(result).accepted();

		assertThat(authorityService.findById(authority.getId())).rejected().withType(ResultType.NOT_FOUND);
	}

	@Test
	public void shouldBeAbleToFindAnAuthorityById() {
		Authority authority = authority().thatIs().persistent().build();

		Result<Authority> result = authorityService.findById(authority.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdIsNullOrEmpty() {
		assertThat(authorityService.findById(null)).rejected().withMessage("The authority id to retrieve cannot be null or empty.");
		assertThat(authorityService.findById("")).rejected().withMessage("The authority id to retrieve cannot be null or empty.");
		assertThat(authorityService.findById("  ")).rejected().withMessage("The authority id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByIdIfTheAuthorityIdDoesNotExist() {
		assertThat(authorityService.findById("csrx")).rejected().withMessage("No such authority id: csrx");
	}

	@Test
	public void shouldBeAbleToFindAnAuthorityByName() {
		Authority authority = authority().thatIs().persistent().build();

		Result<Authority> result = authorityService.findByName(authority.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(authority);
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByNameIfTheAuthorityNameIsNullOrEmpty() {
		assertThat(authorityService.findByName(null)).rejected().withMessage("The authority name to retrieve cannot be null or empty.");
		assertThat(authorityService.findByName("")).rejected().withMessage("The authority name to retrieve cannot be null or empty.");
		assertThat(authorityService.findByName("  ")).rejected().withMessage("The authority name to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnAuthorityByNameIfTheAuthorityNameDoesNotExist() {
		assertThat(authorityService.findByName("csrx")).rejected().withMessage("No such authority name: csrx");
	}

	@Test
	public void shouldBeAbleToListAllAuthorities() {
		range(0, 5).forEach(i -> authority().thatIs().persistent().build());

		Result<List<Authority>> result = authorityService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
