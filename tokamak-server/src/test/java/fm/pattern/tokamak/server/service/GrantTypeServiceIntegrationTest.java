package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class GrantTypeServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private GrantTypeService grantTypeService;

	@Test
	public void shouldBeAbleToCreateAGrantType() {
		GrantType grantType = new GrantType("user");

		Result<GrantType> result = grantTypeService.create(grantType);
		assertThat(result).accepted();

		GrantType created = result.getInstance();
		assertThat(created.getName()).isEqualTo(grantType.getName());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeIfTheGrantTypeIsInvalid() {
		GrantType grantType = grantType().withName(null).build();
		assertThat(grantTypeService.create(grantType)).rejected().withMessage("A grant type name is required.");
	}

	@Test
	public void shouldBeAbleToUpdateAGrantType() {
		GrantType grantType = grantType().save();
		grantType.setName("first");

		assertThat(grantTypeService.update(grantType)).accepted();

		GrantType found = grantTypeService.findById(grantType.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeIfTheGrantTypeIsInvalid() {
		GrantType grantType = grantType().save();
		grantType.setName(null);

		assertThat(grantTypeService.update(grantType)).rejected().withMessage("A grant type name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAGrantType() {
		GrantType grantType = grantType().save();
		assertThat(grantTypeService.findById(grantType.getId())).accepted();

		assertThat(grantTypeService.delete(grantType)).accepted();
		assertThat(grantTypeService.findById(grantType.getId())).rejected();
	}

	@Test
	public void shouldNotBeAbleToDeleteAGrantTypeIfTheGrantTypeIsInvalid() {
		GrantType grantType = grantType().save();
		grantType.setId(null);
		assertThat(grantTypeService.delete(grantType)).rejected().withError("ENT-0001", "An id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToDeleteAnGrantTypeIfTheGrantTypeIsBeingUsedByClients() {
		GrantType grantType = grantType().save();
		client().withGrantType(grantType).withGrantType(grantType().save()).save();

		Result<GrantType> result = grantTypeService.delete(grantType);
		assertThat(result).rejected().withError("GNT-0008", "This grant type cannot be deleted, 1 client is linked to this grant type.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToFindAGrantTypeById() {
		GrantType grantType = grantType().save();

		Result<GrantType> result = grantTypeService.findById(grantType.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(grantType);
	}

	@Test
	public void shouldNotBeAbleToFindAnGrantTypeByIdIfTheGrantTypeIdIsNullOrEmpty() {
		assertThat(grantTypeService.findById(null)).rejected().withError("GNT-0005", "The grant type id to retrieve cannot be null or empty.", UnprocessableEntityException.class);
		assertThat(grantTypeService.findById("")).rejected().withError("GNT-0005", "The grant type id to retrieve cannot be null or empty.", UnprocessableEntityException.class);
		assertThat(grantTypeService.findById("  ")).rejected().withError("GNT-0005", "The grant type id to retrieve cannot be null or empty.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnGrantTypeByIdIfTheGrantTypeIdDoesNotExist() {
		assertThat(grantTypeService.findById("csrx")).rejected().withError("SYS-0001", "No such granttype id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAGrantTypeByName() {
		GrantType grantType = grantType().save();

		Result<GrantType> result = grantTypeService.findByName(grantType.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(grantType);
	}

	@Test
	public void shouldNotBeAbleToFindAGrantTypeByNameIfTheGrantTypeNameIsNullOrEmpty() {
		assertThat(grantTypeService.findByName(null)).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		assertThat(grantTypeService.findByName("")).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		assertThat(grantTypeService.findByName("  ")).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAGrantTypeByNameIfTheGrantTypeNameDoesNotExist() {
		assertThat(grantTypeService.findByName("csrx")).rejected().withError("GNT-0007", "No such grant type name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllGrantTypes() {
		range(0, 5).forEach(i -> grantType().save());

		Result<List<GrantType>> result = grantTypeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

	@Test
	public void shouldBeAbleToFindMultipleGrantTypesById() {
		GrantType grantType1 = grantType().save();
		GrantType grantType2 = grantType().save();
		GrantType grantType3 = grantType().save();

		List<String> ids = new ArrayList<>();
		ids.add(grantType1.getId());
		ids.add(grantType2.getId());
		ids.add(grantType3.getId());

		Result<List<GrantType>> result = grantTypeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(grantType1, grantType2, grantType3);
	}

	@Test
	public void shouldReturnAnEmptyListOfGrantTypesIfTheGrantTypeIdListIsNullOrEmpty() {
		assertThat(grantTypeService.findExistingById(null)).accepted();
		assertThat(grantTypeService.findExistingById(null).getInstance()).isEmpty();

		assertThat(grantTypeService.findExistingById(new ArrayList<String>())).accepted();
		assertThat(grantTypeService.findExistingById(new ArrayList<String>()).getInstance()).isEmpty();
	}

	@Test
	public void shouldIgnoreEmptyGrantTypeEntries() {
		GrantType grantType1 = grantType().save();
		GrantType grantType2 = grantType().save();
		GrantType grantType3 = grantType().save();

		List<String> ids = new ArrayList<>();
		ids.add(grantType1.getId());
		ids.add(null);
		ids.add(grantType2.getId());
		ids.add("");
		ids.add(grantType3.getId());

		Result<List<GrantType>> result = grantTypeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).hasSize(3);
		assertThat(result.getInstance()).contains(grantType1, grantType2, grantType3);
	}

	@Test
	public void shouldReturnAnEmtpyListWhenAllAuthoritiesAreNullOrEmpty() {
		List<String> ids = new ArrayList<>();
		ids.add(null);
		ids.add("");

		Result<List<GrantType>> result = grantTypeService.findExistingById(ids);
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEmpty();
	}

}
