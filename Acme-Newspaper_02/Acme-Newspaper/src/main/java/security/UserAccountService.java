
package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {

	/* Repository */

	@Autowired
	private UserAccountRepository	userAccountRepository;


	/* Methods */

	public UserAccount create() {

		final UserAccount res = new UserAccount();
		res.setAuthorities(new ArrayList<Authority>());
		return res;

	}

	public UserAccount findOne(final int userAccountId) {
		return this.userAccountRepository.findOne(userAccountId);
	}

	public Collection<UserAccount> findAll() {
		return this.userAccountRepository.findAll();
	}

	public UserAccount save(final UserAccount userAccount) {
		return this.userAccountRepository.save(userAccount);
	}

	public void delete(final UserAccount userAccount) {
		this.userAccountRepository.delete(userAccount);
	}

}
