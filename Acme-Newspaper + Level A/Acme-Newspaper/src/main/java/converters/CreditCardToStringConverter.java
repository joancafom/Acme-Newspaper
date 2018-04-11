
package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard creditCard) {
		String res;
		StringBuilder builder;

		if (creditCard == null)
			res = null;
		else
			try {
				builder = new StringBuilder();

				builder.append(URLEncoder.encode(creditCard.getHolderName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getBrandName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getNumber(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(creditCard.getCVV()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(creditCard.getMonth()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(creditCard.getYear()), "UTF-8"));

				res = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return res;
	}

}
