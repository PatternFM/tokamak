package fm.pattern.tokamak.authorization;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class UtilityClassAssertions extends AbstractAssert<UtilityClassAssertions, Class<?>> {

	public UtilityClassAssertions(Class<?> clazz) {
		super(clazz, UtilityClassAssertions.class);
	}

	public static UtilityClassAssertions assertThat(Class<?> clazz) {
		return new UtilityClassAssertions(clazz);
	}

	public UtilityClassAssertions isAWellDefinedUtilityClass() {
		Assertions.assertThat(Modifier.isFinal(actual.getModifiers())).describedAs("A utility class should be marked as final.").isTrue();
		Assertions.assertThat(actual.getDeclaredConstructors().length).describedAs("A utility class should only have one constructor, but this class has " + actual.getDeclaredConstructors().length).isEqualTo(1);

		try {
			Constructor<?> constructor = actual.getDeclaredConstructor();
			if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
				Assertions.fail("The constructor is not private.");
			}

			constructor.setAccessible(true);
			constructor.newInstance();
			constructor.setAccessible(false);

			for (Method method : actual.getMethods()) {
				if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(actual)) {
					Assertions.fail("A utility class should not have instance methods, but found: " + method);
				}
			}
		}
		catch (Exception e) {
			Assertions.fail("An error occurred while inspecting the class.");
		}

		return this;
	}

}
