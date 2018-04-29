
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ANMessageRepository;
import domain.ANMessage;

@Component
@Transactional
public class StringToANMessageConverter implements Converter<String, ANMessage> {

	@Autowired
	private ANMessageRepository	anMessageRepository;


	@Override
	public ANMessage convert(final String text) {
		final ANMessage result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.anMessageRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
