package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.ScopeDSL.scope;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ResultType;

public class ScopeServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private ScopeService scopeService;

	@Test
	public void shouldBeAbleToCreateAScope() {
		Scope scope = new Scope("user");

		Result<Scope> result = scopeService.create(scope);
		assertThat(result).accepted();

		Scope created = result.getInstance();
		assertThat(created.getName()).isEqualTo(scope.getName());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeIfTheScopeIsInvalid() {
		Scope scope = new Scope(null);

		Result<Scope> result = scopeService.create(scope);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToUpdateAScope() {
		Scope scope = scope().thatIs().persistent().build();
		scope.setName("first");

		Result<Scope> result = scopeService.update(scope);
		assertThat(result).accepted();

		Scope found = scopeService.findById(scope.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeIfTheScopeIsInvalid() {
		Scope scope = scope().thatIs().persistent().build();
		scope.setName(null);

		Result<Scope> result = scopeService.update(scope);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToDeleteAScope() {
		Scope scope = scope().thatIs().persistent().build();
		assertThat(scopeService.findById(scope.getId())).accepted();

		Result<Scope> result = scopeService.delete(scope);
		assertThat(result).accepted();

		assertThat(scopeService.findById(scope.getId())).rejected().withType(ResultType.NOT_FOUND);
	}

	@Test
	public void shouldBeAbleToFindAScopeById() {
		Scope scope = scope().thatIs().persistent().build();

		Result<Scope> result = scopeService.findById(scope.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(scope);
	}

	@Test
	public void shouldNotBeAbleToFindAnScopeByIdIfTheScopeIdIsNullOrEmpty() {
		assertThat(scopeService.findById(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope id to retrieve cannot be null or empty.");
		assertThat(scopeService.findById("")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope id to retrieve cannot be null or empty.");
		assertThat(scopeService.findById("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnScopeByIdIfTheScopeIdDoesNotExist() {
		assertThat(scopeService.findById("csrx")).rejected().withType(NOT_FOUND).withMessage("No such scope id: csrx");
	}

	@Test
	public void shouldBeAbleToFindAScopeByName() {
		Scope scope = scope().thatIs().persistent().build();

		Result<Scope> result = scopeService.findByName(scope.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(scope);
	}

	@Test
	public void shouldNotBeAbleToFindAScopeByNameIfTheScopeNameIsNullOrEmpty() {
		assertThat(scopeService.findByName(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope name to retrieve cannot be null or empty.");
		assertThat(scopeService.findByName("")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope name to retrieve cannot be null or empty.");
		assertThat(scopeService.findByName("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The scope name to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAScopeByNameIfTheScopeNameDoesNotExist() {
		assertThat(scopeService.findByName("csrx")).rejected().withType(NOT_FOUND).withMessage("No such scope name: csrx");
	}

	@Test
	public void shouldBeAbleToListAllScopes() {
		range(0, 5).forEach(i -> scope().thatIs().persistent().build());

		Result<List<Scope>> result = scopeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
