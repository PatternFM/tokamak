package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class GrantTypeValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidGrantType() {
		onCreate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onCreate(grantType().withName(null).build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		onCreate(grantType().withName("").build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		onCreate(grantType().withName("  ").build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onCreate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("GNT-0002", "A grant type name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		grantType().withName(name).thatIs().persistent().build();

		onCreate(grantType().withName(name).build()).rejected().withError("GNT-0003", "This grant type name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidGrantType() {
		onUpdate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onUpdate(grantType().withName(null).build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		onUpdate(grantType().withName("").build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
		onUpdate(grantType().withName("  ").build()).rejected().withError("GNT-0001", "A grant type name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onUpdate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("GNT-0002", "A grant type name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		grantType().withName(name).thatIs().persistent().build();

		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName(name);

		onUpdate(grantType).rejected().withError("GNT-0003", "This grant type name is already in use.", ResourceConflictException.class);
	}

}
