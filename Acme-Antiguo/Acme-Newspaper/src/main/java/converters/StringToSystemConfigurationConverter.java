
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Component
@Transactional
public class StringToSystemConfigurationConverter implements Converter<String, SystemConfiguration> {

	@Autowired
	SystemConfigurationRepository	systemConfigurationRepository;


	@Override
	public SystemConfiguration convert(final String text) {
		SystemConfiguration result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.systemConfigurationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
