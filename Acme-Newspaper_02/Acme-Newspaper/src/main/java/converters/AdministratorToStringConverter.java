
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Administrator;

@Component
@Transactional
public class AdministratorToStringConverter implements Converter<Administrator, String> {

	@Override
	public String convert(final Administrator administrator) {
		final String result;

		if (administrator == null)
			result = null;
		else
			result = String.valueOf(administrator.getId());

		return result;
	}
}
