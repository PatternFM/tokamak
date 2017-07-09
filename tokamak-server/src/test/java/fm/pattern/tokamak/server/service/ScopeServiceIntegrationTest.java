package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.tokamak.server.dsl.ScopeDSL.scope;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class ScopeServiceIntegrationTest extends IntegrationTest {

	@Autowired
	@Qualifier("clientCache")
	private Cache cache;

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
		Scope scope = scope().save();
		scope.setName("first");

		assertThat(scopeService.update(scope)).accepted();

		Scope found = scopeService.findById(scope.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldFlushTheClientCacheWhenAScopeIsUpdated() {
		Client client = client().withGrantType(grantType().save()).save();

		assertThat(cache.get("clients:id:" + client.getId(), Client.class)).isNotNull();
		assertThat(scopeService.update(scope().save())).accepted();
		assertThat(cache.get("clients:id:" + client.getId(), Client.class)).isNull();
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeIfTheScopeIsInvalid() {
		Scope scope = scope().save();
		scope.setName(null);

		assertThat(scopeService.update(scope)).rejected().withMessage("A scope name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAScope() {
		Scope scope = scope().save();
		assertThat(scopeService.findById(scope.getId())).accepted();

		assertThat(scopeService.delete(scope)).accepted();
		assertThat(scopeService.findById(scope.getId())).rejected();
	}

	@Test
	public void shouldNotBeAbleToDeleteAScopeIfTheScopeIsInvalid() {
		Scope scope = scope().save();
		scope.setId(null);
		assertThat(scopeService.delete(scope)).rejected().withError("ENT-0001", "An id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToDeleteAScopeIfTheScopeIsBeingUsedByClients() {
		Scope scope = scope().save();
		client().withScope(scope).withGrantType(grantType().save()).save();

		Result<Scope> result = scopeService.delete(scope);
		assertThat(result).rejected().withError("SCO-0008", "This scope cannot be deleted, 1 client is linked to this scope.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToFindAScopeById() {
		Scope scope = scope().save();

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
		Scope scope = scope().save();

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
		range(0, 5).forEach(i -> scope().save());

		Result<List<Scope>> result = scopeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

	@Test
	public void shouldBeAbleToFindMultipleScopesById() {
		Scope scope1 = scope().save();
		Scope scope2 = scope().save();
		Scope scope3 = scope().save();

		List<String> ids = new ArrayList<>();
		ids.add(scope1.getId());
		ids.add(scope2.getId());
		ids.add(scope3.getId());

		Result<List<Scope>> result = scopeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(scope1, scope2, scope3);
	}

	@Test
	public void shouldReturnAnEmptyListOfScopesIfTheScopeIdListIsNullOrEmpty() {
		assertThat(scopeService.findExistingById(null)).accepted();
		assertThat(scopeService.findExistingById(null).getInstance()).isEmpty();

		assertThat(scopeService.findExistingById(new ArrayList<String>())).accepted();
		assertThat(scopeService.findExistingById(new ArrayList<String>()).getInstance()).isEmpty();
	}

	@Test
	public void shouldIgnoreEmptyScopeEntries() {
		Scope scope1 = scope().save();
		Scope scope2 = scope().save();
		Scope scope3 = scope().save();

		List<String> ids = new ArrayList<>();
		ids.add(scope1.getId());
		ids.add(null);
		ids.add(scope2.getId());
		ids.add("");
		ids.add(scope3.getId());

		Result<List<Scope>> result = scopeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(scope1, scope2, scope3);
	}

	@Test
	public void shouldReturnAnEmtpyListWhenAllScopesAreNullOrEmpty() {
		List<String> ids = new ArrayList<>();
		ids.add(null);
		ids.add("");

		Result<List<Scope>> result = scopeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEmpty();
	}

}
