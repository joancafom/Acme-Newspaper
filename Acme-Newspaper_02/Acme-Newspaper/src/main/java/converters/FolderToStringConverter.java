
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Folder;

@Component
@Transactional
public class FolderToStringConverter implements Converter<Folder, String> {

	@Override
	public String convert(final Folder folder) {
		String res;

		if (folder == null)
			res = null;
		else
			res = String.valueOf(folder.getId());

		return res;
	}

}
