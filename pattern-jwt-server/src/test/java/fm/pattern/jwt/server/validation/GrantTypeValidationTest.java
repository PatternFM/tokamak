package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.GrantType;

public class GrantTypeValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidGrantType() {
		onCreate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onCreate(grantType().withName(null).build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
		onCreate(grantType().withName("").build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
		onCreate(grantType().withName("  ").build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onCreate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("GNT-0002").withMessage("A grant type name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		grantType().withName(name).thatIs().persistent().build();
		
		onCreate(grantType().withName(name).build()).rejected().withCode("GNT-0003").withMessage("This grant type name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidGrantType() {
		onUpdate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onUpdate(grantType().withName(null).build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
		onUpdate(grantType().withName("").build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
		onUpdate(grantType().withName("  ").build()).rejected().withCode("GNT-0001").withMessage("A grant type name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onUpdate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("GNT-0002").withMessage("A grant type name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		grantType().withName(name).thatIs().persistent().build();

		GrantType grantType = grantType().thatIs().persistent().build();
		grantType.setName(name);

		onUpdate(grantType).rejected().withCode("GNT-0003").withMessage("This grant type name is already in use.");
	}

}
