
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import services.FolderService;
import domain.Folder;

public class StringToFolderConverter implements Converter<String, Folder> {

	@Autowired
	private FolderService	folderService;


	@Override
	public Folder convert(final String text) {

		Folder res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.folderService.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return res;
	}
}
