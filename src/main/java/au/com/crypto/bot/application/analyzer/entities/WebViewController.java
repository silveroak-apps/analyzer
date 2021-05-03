package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Transactional
@Component
public class WebViewController {

	@Autowired
	WebViewRepository webViewRepository;


	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;

	public List<WebView> findAll() {
		Query q = em.createNativeQuery("SELECT *" +
				" FROM public.webview ",WebView.class);
		return  Collections.checkedList(q.getResultList(), WebView.class);
	}
}