
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import security.LoginService;
import domain.Administrator;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Service
@Transactional
public class ArticleService {

	/* Managed Repository */
	@Autowired
	private ArticleRepository			articleRepository;

	// Supporting Services ---------------------------------------------------

	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private UserService					userService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Validator -------------------------------------------------------------

	@Autowired
	private Validator					validator;


	// CRUD Methods ----------------------------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA
	public Article create(final Newspaper newspaper) {
		Assert.notNull(newspaper);
		Assert.isNull(newspaper.getPublicationDate());

		final Article article = new Article();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		article.setContainsTaboo(false);
		article.setNewspaper(newspaper);
		article.setWriter(user);

		final Collection<Article> followUps = new HashSet<Article>();
		article.setFollowUps(followUps);

		final Collection<String> pictures = new HashSet<String>();
		article.setPictures(pictures);

		return article;
	}

	// v1.0 - Implemented by JA
	public Article create(final Article mainArticle) {

		Assert.notNull(mainArticle);
		Assert.isTrue(mainArticle.getIsFinal());
		Assert.notNull(mainArticle.getNewspaper());
		Assert.notNull(mainArticle.getNewspaper().getPublicationDate());

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.isTrue(user.equals(mainArticle.getWriter()));

		final Article article = new Article();

		article.setContainsTaboo(false);
		article.setMainArticle(mainArticle);
		article.setWriter(user);

		mainArticle.getFollowUps().add(article);

		final Collection<Article> followUps = new HashSet<Article>();
		article.setFollowUps(followUps);

		final Collection<String> pictures = new HashSet<String>();
		article.setPictures(pictures);

		return article;
	}

	/* v1.0 - josembell */
	public Collection<Article> findAll() {
		return this.articleRepository.findAll();
	}

	/* v1.0 - josembell */
	public Article findOne(final int articleId) {
		return this.articleRepository.findOne(articleId);
	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Modified by JA (taboo)
	public Article save(final Article article) {
		Assert.notNull(article);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(article.getWriter().equals(user));

		Article oldArticle = null;

		if (article.getId() != 0) {
			oldArticle = this.articleRepository.findOne(article.getId());
			Assert.isTrue(!oldArticle.getIsFinal());
			Assert.isTrue(oldArticle.getWriter().equals(article.getWriter()));
			Assert.isNull(oldArticle.getPublicationDate());
		}

		Assert.notNull(article.getPictures());
		if (!article.getPictures().isEmpty())
			for (final String s : article.getPictures())
				try {
					@SuppressWarnings("unused")
					final URL url = new java.net.URL(s);
				} catch (final MalformedURLException e) {
					throw new IllegalArgumentException();
				}

		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(article.getTitle() + " " + article.getSummary() + " " + article.getBody());
		article.setContainsTaboo(containsTabooVeredict);

		return this.articleRepository.save(article);
	}
	// v1.0 - Implemented by JA
	public void delete(final Article article) {

		Assert.notNull(article);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		for (final Article followUp : article.getFollowUps())
			followUp.setMainArticle(null);

		article.getNewspaper().getArticles().remove(article);

		this.articleRepository.delete(article);
	}

	//Other Business Methods -------------------

	/* v1.0 - josembell */
	public Collection<Article> findPublishedByKeyword(final String keyword) {
		return this.articleRepository.findPublishedByKeyword(keyword);
	}

	// v1.0 - Implemented by JA
	public Collection<Article> findTabooedArticles() {
		return this.articleRepository.findTabooedArticles();
	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.articleRepository.flush();
	}

	// v1.0 - Implemented by Alicia
	public Collection<Article> getAllFinalByNewspaper(final Newspaper newspaper) {
		Assert.notNull(newspaper);

		final Collection<Article> res = this.articleRepository.getAllFinalByNewspaperId(newspaper.getId());
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Collection<Article> getPublisedArticles(final User writer) {

		Assert.notNull(writer);

		final Collection<Article> res = this.articleRepository.publishedArticlesByWriterId(writer.getId());
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Collection<Article> getUnpublisedArticles(final User writer, final Newspaper newspaper) {

		Assert.notNull(writer);
		Assert.notNull(newspaper);

		final Collection<Article> res = this.articleRepository.unpublishedArticlesByWriterNewspaperId(writer.getId(), newspaper.getId());
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by Alicia
	public Article reconstructDelete(final Article prunedArticle, final BindingResult binding) {
		Article res;

		Assert.notNull(prunedArticle);
		res = this.findOne(prunedArticle.getId());

		Assert.notNull(res);
		this.validator.validate(res, binding);

		return res;
	}

	// v1.0 - Implemented by Alicia
	public Article reconstructSave(final Article prunedArticle, final BindingResult binding) {
		final Article res;

		final User writer = this.userService.findByUserAccount(LoginService.getPrincipal());

		res = prunedArticle;
		res.setWriter(writer);

		this.validator.validate(res, binding);

		return res;
	}
}
