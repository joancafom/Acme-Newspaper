
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ArticleRepository;
import domain.Article;

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
}
