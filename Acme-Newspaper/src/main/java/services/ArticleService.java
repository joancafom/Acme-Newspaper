
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ArticleRepository;
import domain.Article;
import domain.User;

@Service
@Transactional
public class ArticleService {

	/* Managed Repository */
	@Autowired
	private ArticleRepository	articleRepository;


	/* v1.0 - josembell */
	public Collection<Article> findAll() {
		return this.articleRepository.findAll();
	}

	/* v1.0 - josembell */
	public Article findOne(final int articleId) {
		return this.articleRepository.findOne(articleId);
	}

	//Other Business Methods -------------------

	//v1.0 - Implemented by JA
	public Collection<Article> getPublisedArticles(final User writer) {

		Assert.notNull(writer);

		final Collection<Article> res = this.articleRepository.publishedArticlesByWriterId(writer.getId());
		Assert.notNull(res);

		return res;

	}
}
