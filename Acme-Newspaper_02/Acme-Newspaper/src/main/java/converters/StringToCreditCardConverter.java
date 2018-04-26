
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Override
	public CreditCard convert(final String text) {
		CreditCard res;
		String parts[];

		if (text == null || StringUtils.isEmpty(text))
			res = null;
		else
			try {
				parts = text.split("\\|");
				res = new CreditCard();
				res.setHolderName(URLDecoder.decode(parts[0], "UTF-8"));
				res.setBrandName(URLDecoder.decode(parts[1], "UTF-8"));
				res.setNumber(URLDecoder.decode(parts[2], "UTF-8"));
				res.setCVV(Integer.valueOf(URLDecoder.decode(parts[3], "UTF-8")));
				res.setMonth(Integer.valueOf(URLDecoder.decode(parts[4], "UTF-8")));
				res.setYear(Integer.valueOf(URLDecoder.decode(parts[5], "UTF-8")));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return res;
	}
}
