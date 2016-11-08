package pl.pvkk.profit.user.pocket;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.pvkk.profit.shares.Quotation;
import pl.pvkk.profit.shares.Share;
import pl.pvkk.profit.user.trades.Transaction;

@Service
public class PocketService {

	@Autowired
	private PocketDao pocketDao;


	public Pocket getPocketById(String username) {
		return pocketDao.getPocketById(username);
	}

	public boolean isShareExistsInPocket(Pocket pocket, String shareShortcut, int shareNumber) {
		Map<String, Integer> shares = pocket.getShares();

		if(!shares.containsKey(shareShortcut.toUpperCase()))
				return false;
		else if(shares.get(shareShortcut) < shareNumber)
			return false;
		return true;
	}

	@Transactional
	public void setSharesAndTransactions(Pocket pocket, Share share, int shareNumber) {
		Map<String, Integer> shares = pocket.getShares();
		String shareShortcut = share.getShortcut();
		List<Quotation> quotations = share.getQuotations();
		double sharePrice = quotations.get(quotations.size()-1).getReferencePrice();
		
		if (!shares.containsKey(shareShortcut))
			shares.put(shareShortcut, shareNumber);
		else {
			int shareFromPocketNumber = shares.get(shareShortcut);

			if (shareNumber + shareFromPocketNumber == 0)
				shares.remove(shareShortcut);
			else
				shares.replace(shareShortcut, shareNumber + shareFromPocketNumber);
		}
		
		Transaction transaction = new Transaction();
		transaction.setBuyer(pocket);
		transaction.setShare(share);
		transaction.setShare_number(shareNumber);
		transaction.setShare_price(sharePrice);
		transaction.setDate(new Date());
		
		pocket.setShares(shares);
		BigDecimal cost = BigDecimal.valueOf(sharePrice*shareNumber);
		pocket.setMoney(pocket.getMoney().subtract(cost));
		pocketDao.updateSharesAndMoneyInPocket(pocket, transaction);
	}

}
