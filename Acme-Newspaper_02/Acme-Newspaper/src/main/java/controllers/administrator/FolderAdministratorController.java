
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ANMessageService;
import services.ActorService;
import services.FolderService;
import controllers.AbstractController;
import domain.ANMessage;
import domain.Actor;
import domain.Folder;

@Controller
@RequestMapping("/folder/administrator")
public class FolderAdministratorController extends AbstractController {

	private final String		ACTOR_WS	= "administrator/";

	/* Services */
	@Autowired
	private FolderService		folderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ANMessageService	anMessageService;


	public FolderAdministratorController() {
		super();
	}

	// v1.0 - Unknown
	// v2.0 - Updated by Alicia
	/* v3.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-444792-p", defaultValue = "1") final Integer page, @RequestParam(value = "d-3565872-p", defaultValue = "1") final Integer pageMessages, @RequestParam(required = false) final Integer folderId) {
		final ModelAndView result;
		Collection<Folder> folders;
		Collection<ANMessage> messages;
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final String requestURI = "folder/administrator/list.do";

		if (folderId == null) {
			final Page<Folder> pageResult = this.folderService.findAllParentFoldersByPrincipal(page, 5);
			folders = pageResult.getContent();
			final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

			result = new ModelAndView("folder/list");
			result.addObject("folders", folders);
			result.addObject("resultSize", resultSize);

		} else {

			final Folder parentFolder = this.folderService.findOne(folderId);
			Assert.notNull(parentFolder);
			Assert.isTrue(parentFolder.getActor().equals(actor));

			final Page<Folder> pageResult = this.folderService.findChildFoldersOfFolderByPrincipal(page, 5, parentFolder);
			folders = pageResult.getContent();
			final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

			final Page<ANMessage> pageResultMessages = this.anMessageService.findMessagesByFolder(pageMessages, 5, parentFolder);
			messages = pageResultMessages.getContent();
			final Integer resultSizeMessages = new Long(pageResultMessages.getTotalElements()).intValue();

			result = new ModelAndView("folder/list");
			result.addObject("anMessages", messages);
			result.addObject("folder", parentFolder);
			result.addObject("resultSize", resultSize);
			result.addObject("resultSizeMessages", resultSizeMessages);
		}
		result.addObject("folderId", folderId);
		result.addObject("folders", folders);
		result.addObject("requestURI", requestURI);

		result.addObject("actorWS", this.ACTOR_WS);

		return result;

	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) final Integer folderId) {
		final ModelAndView result;
		Folder folder;
		final Folder folderParent;

		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		if (folderId != null) {
			folderParent = this.folderService.findOne(folderId);
			folder = this.folderService.create(actor, folderParent);
		} else
			folder = this.folderService.create(actor, null);

		result = this.createEditModelAndView(folder);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;

	}

	/* v1.0 - josembell */
	//v2.0 - Modified by JA
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		folder = this.folderService.findOne(folderId);
		Assert.notNull(folder);
		Assert.isTrue(folder.getActor().equals(actor));
		Assert.isTrue(!folder.getIsSystem());

		result = this.createEditModelAndView(folder);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Folder prunedFolder, final BindingResult binding) {
		ModelAndView result;

		final Folder folder = this.folderService.reconstruct(prunedFolder, binding);

		try {
			this.folderService.deleteByPrincipal(folder);
			result = new ModelAndView("redirect:/folder/administrator/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(folder, "folder.commit.error");
		}

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Folder prunedFolder, final BindingResult binding) {
		ModelAndView result;

		final Folder folder = this.folderService.reconstruct(prunedFolder, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				this.folderService.save(folder);
				result = new ModelAndView("redirect:/folder/administrator/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(folder, "folder.commit.error");
			}

		return result;

	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;
		result = this.createEditModelAndView(folder, null);
		return result;
	}

	/* v1.0 - josembell */
	//v2.0 - Modified by JA (Laziness)
	//v3.0 - Modified by JA (moving)
	protected ModelAndView createEditModelAndView(final Folder folder, final String message) {
		ModelAndView result;

		Assert.notNull(folder);
		result = new ModelAndView("folder/edit");
		result.addObject("folder", folder);
		result.addObject("message", message);

		//We add a list of folders we can move to in case it is an edition
		if (folder.getId() != 0) {
			final Collection<Folder> folders = this.folderService.findCompatibleFoldersToMove(folder);
			result.addObject("folders", folders);
		}

		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}
}
