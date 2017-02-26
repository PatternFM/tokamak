package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ResultType;

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
		GrantType grantType = new GrantType(null);

		Result<GrantType> result = grantTypeService.create(grantType);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToUpdateAGrantType() {
		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName("first");

		Result<GrantType> result = grantTypeService.update(grantType);
		assertThat(result).accepted();

		GrantType found = grantTypeService.findById(grantType.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeIfTheGrantTypeIsInvalid() {
		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName(null);

		Result<GrantType> result = grantTypeService.update(grantType);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToDeleteAGrantType() {
		GrantType grantType = grantType().thatIs().persistent().build();
		assertThat(grantTypeService.findById(grantType.getId())).accepted();

		Result<GrantType> result = grantTypeService.delete(grantType);
		assertThat(result).accepted();

		assertThat(grantTypeService.findById(grantType.getId())).rejected().withType(ResultType.NOT_FOUND);
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
		assertThat(grantTypeService.findById(null)).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type id to retrieve cannot be null or empty.");
		assertThat(grantTypeService.findById("")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type id to retrieve cannot be null or empty.");
		assertThat(grantTypeService.findById("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnGrantTypeByIdIfTheGrantTypeIdDoesNotExist() {
		assertThat(grantTypeService.findById("csrx")).rejected().withType(NOT_FOUND).withDescription("No such granttype id: csrx");
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
		assertThat(grantTypeService.findByName(null)).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type name to retrieve cannot be null or empty.");
		assertThat(grantTypeService.findByName("")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type name to retrieve cannot be null or empty.");
		assertThat(grantTypeService.findByName("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("The grant type name to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAGrantTypeByNameIfTheGrantTypeNameDoesNotExist() {
		assertThat(grantTypeService.findByName("csrx")).rejected().withType(NOT_FOUND).withDescription("No such grant type name: csrx");
	}

	@Test
	public void shouldBeAbleToListAllGrantTypes() {
		range(0, 5).forEach(i -> grantType().thatIs().persistent().build());

		Result<List<GrantType>> result = grantTypeService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
