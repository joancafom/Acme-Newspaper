
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ANMessage;

@Component
@Transactional
public class ANMessageToStringConverter implements Converter<ANMessage, String> {

	@Override
	public String convert(final ANMessage anMessage) {
		String result;

		if (anMessage == null)
			result = null;
		else
			result = String.valueOf(anMessage.getId());
		return result;
	}

}
