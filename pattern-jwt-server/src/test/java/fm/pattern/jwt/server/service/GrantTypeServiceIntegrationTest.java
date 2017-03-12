package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.valex.EntityNotFoundException;
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
		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName("first");

		assertThat(grantTypeService.update(grantType)).accepted();

		GrantType found = grantTypeService.findById(grantType.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeIfTheGrantTypeIsInvalid() {
		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName(null);

		assertThat(grantTypeService.update(grantType)).rejected().withMessage("A grant type name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAGrantType() {
		GrantType grantType = grantType().thatIs().persistent().build();
		assertThat(grantTypeService.findById(grantType.getId())).accepted();

		assertThat(grantTypeService.delete(grantType)).accepted();
		assertThat(grantTypeService.findById(grantType.getId())).rejected();
	}

	@Test
	public void shouldBeAbleToFindAGrantTypeById() {
		GrantType grantType = grantType().thatIs().persistent().build();

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
		GrantType grantType = grantType().thatIs().persistent().build();

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
		range(0, 5).forEach(i -> grantType().thatIs().persistent().build());

		Result<List<GrantType>> result = grantTypeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
