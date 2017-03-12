package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.ScopeDSL.scope;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

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
		Scope scope = scope().withName(null).build();
		assertThat(scopeService.create(scope)).rejected().withMessage("A scope name is required.");
	}

	@Test
	public void shouldBeAbleToUpdateAScope() {
		Scope scope = scope().thatIs().persistent().build();
		scope.setName("first");

		assertThat(scopeService.update(scope)).accepted();

		Scope found = scopeService.findById(scope.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeIfTheScopeIsInvalid() {
		Scope scope = scope().thatIs().persistent().build();
		scope.setName(null);

		assertThat(scopeService.update(scope)).rejected().withMessage("A scope name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAScope() {
		Scope scope = scope().thatIs().persistent().build();
		assertThat(scopeService.findById(scope.getId())).accepted();

		assertThat(scopeService.delete(scope)).accepted();
		assertThat(scopeService.findById(scope.getId())).rejected();
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
		assertThat(scopeService.findById(null)).rejected().withError("SCO-0005", "A scope id is required.", UnprocessableEntityException.class);
		assertThat(scopeService.findById("")).rejected().withError("SCO-0005", "A scope id is required.", UnprocessableEntityException.class);
		assertThat(scopeService.findById("  ")).rejected().withError("SCO-0005", "A scope id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnScopeByIdIfTheScopeIdDoesNotExist() {
		assertThat(scopeService.findById("csrx")).rejected().withError("SYS-0001", "No such scope id: csrx", EntityNotFoundException.class);
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
		assertThat(scopeService.findByName(null)).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		assertThat(scopeService.findByName("")).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		assertThat(scopeService.findByName("  ")).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAScopeByNameIfTheScopeNameDoesNotExist() {
		assertThat(scopeService.findByName("csrx")).rejected().withError("SCO-0007", "No such scope name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllScopes() {
		range(0, 5).forEach(i -> scope().thatIs().persistent().build());

		Result<List<Scope>> result = scopeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
